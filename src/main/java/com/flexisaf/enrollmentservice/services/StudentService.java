package com.flexisaf.enrollmentservice.services;

import com.flexisaf.enrollmentservice.dto.requests.StudentRequest;
import com.flexisaf.enrollmentservice.dto.requests.StudentRequestParam;
import com.flexisaf.enrollmentservice.dto.responses.ResponseList;
import com.flexisaf.enrollmentservice.dto.responses.StudentResponse;
import com.flexisaf.enrollmentservice.exceptions.BadRequestException;
import com.flexisaf.enrollmentservice.jobs.BirthdayNotifier;
import com.flexisaf.enrollmentservice.models.Student;
import com.flexisaf.enrollmentservice.repositories.StudentRepository;
import com.flexisaf.enrollmentservice.utilities.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Transactional
public class StudentService extends BaseService<Student, StudentRepository, Long> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Scheduler scheduler;

    public StudentService(StudentRepository repository) {
        super(repository);
    }

    public boolean deleteStudent (Long studentId){
        return deleteById(studentId);
    }

    public StudentResponse editStudentDetails(Map<String, Object> studentDetails, Long studentId){
        Student editedStudent = editModel(studentDetails, studentId);

        return convertStudentToResponse(editedStudent);
    }


    public StudentResponse enrollStudent(StudentRequest studentRequest) {
        Student student = convertFromRequest(studentRequest);

        if (studentRequest.getDateOfBirth() == null)
            throw new BadRequestException("Date of Birth Should be inputted");

        boolean isAboveOrEighteenYears = student.getDateOfBirth().plusYears(18).isBefore(LocalDate.now());
        boolean isBelowOrTwentyFiveYears = student.getDateOfBirth().plusYears(25).isAfter(LocalDate.now());

        if (!isAboveOrEighteenYears || !isBelowOrTwentyFiveYears) {
            if (!isAboveOrEighteenYears)
                throw new BadRequestException("Age must be Above or Equal to 18 years");
            throw new BadRequestException("Age must be Below or Equal to 25 years");
        }

        Student savedStudent = save(student);

        System.out.println(savedStudent);
        String matricNumber = generateMatricNumber(savedStudent.getId());
        savedStudent.setMatricNumber(matricNumber);

        String encodedMatricNumberAsPassword = passwordEncoder.encode(matricNumber);
        savedStudent.setPassword(encodedMatricNumberAsPassword);

        savedStudent = editModel(savedStudent);


        Student finalSavedStudent = savedStudent;
        CompletableFuture.runAsync(()->{
            JobDetail jobDetail = BirthdayNotifier.buildJobDetail(finalSavedStudent.getId());
            Trigger trigger = BirthdayNotifier.buildJobTrigger(jobDetail, ZonedDateTime.of(finalSavedStudent.getDateOfBirth().atStartOfDay(),
                    ZoneId.of("Etc/GMT+1")));

            try {
                scheduler.scheduleJob(jobDetail, trigger);
                log.info("Trigger For Email Reminder Created Successfully");
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });


        return convertStudentToResponse(savedStudent);

    }

    @Transactional(readOnly = true)
    public StudentResponse retrieveStudent(Long studentId){
        Student student = findById(studentId);

        return convertStudentToResponse(student);
    }

    public Optional<Student> retrieveStudentByMatricNumber(String matricNumber){
        return getRepository().findByMatricNumber(matricNumber);
    }

    @Transactional(readOnly = true)
    public ResponseList<StudentResponse> retrieveAllStudent(StudentRequestParam requestParam) {
        requestParam.validate();
        Pageable pageable = PageRequest.of(Integer.valueOf(requestParam.getCurrentPage()), Integer.valueOf(requestParam.getSize()),
                Sort.by(Sort.Direction.valueOf(requestParam.getDirection()), requestParam.getAttributes().stream().toArray(String[]::new)));

        Page<Student> page = getRepository().findAll((root, query, criteriaBuilder)-> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(requestParam.getFirstName()))
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("firstName"), requestParam.getFirstName()+"%")));

            if (StringUtils.hasText(requestParam.getLastName()))
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("lastName"), requestParam.getLastName()+"%")));

            if (StringUtils.hasText(requestParam.getOtherName()))
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("otherName"), requestParam.getOtherName()+"%")));


            if (requestParam.getCreatedAtFrom()!=null && requestParam.getCreatedAtTo()!=null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"),
                        requestParam.getCreatedAtFrom(),
                        requestParam.getCreatedAtTo()));
            }


            if (requestParam.getGender()!=null)
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("gender"), requestParam.getGender())));


            if (StringUtils.hasText(requestParam.getDepartmentName()))
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("department").get("name"), requestParam.getDepartmentName())));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, pageable);

        return ResponseList.createResponseList(page, this::convertStudentToResponse);
    }


    public StudentResponse convertStudentToResponse(Student student){
        StudentResponse studentResponse = new StudentResponse();
        ModelMapper.mapNotNullValues(student, studentResponse);

        return studentResponse;
    }

    public Student convertFromRequest(StudentRequest studentRequest){
        Student student = new Student();
        ModelMapper.mapNotNullValues(studentRequest, student);
        return student;
    }

    public String generateMatricNumber(Long studentId){
        return String.format("FLEXISAF/%03d", studentId);
    }

    public boolean matchesMatricNumberPattern(String pattern){
        return pattern.matches("FLEXISAF/\\d{3}");
    }


}
