package com.flexisaf.enrollmentservice.controllers;

import com.flexisaf.enrollmentservice.dto.requests.StudentRequest;
import com.flexisaf.enrollmentservice.dto.requests.StudentRequestParam;
import com.flexisaf.enrollmentservice.dto.responses.ResponseList;
import com.flexisaf.enrollmentservice.dto.responses.StudentResponse;
import com.flexisaf.enrollmentservice.exceptions.ForbiddenRequestException;
import com.flexisaf.enrollmentservice.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/students")
@Slf4j
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/{student-id}")
    public ResponseEntity deleteStudent(@PathVariable("student-id") Long studentId){
        boolean deleted = studentService.deleteStudent(studentId);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed("ROLE_ADMIN")
    @PatchMapping("/{student-id}")
    public StudentResponse editStudentDetails(@PathVariable("student-id") Long studentId,
                                              @RequestBody Map<String, Object> studentDetails, HttpServletRequest request){
        return studentService.editStudentDetails(studentDetails, studentId);
    }

    @RolesAllowed("ROLE_STUDENT")
    @PatchMapping
    public StudentResponse editOwnStudentDetails(@RequestBody Map<String, Object> studentDetails, HttpServletRequest request){
        Long studentId = (Long)request.getAttribute("user-id");

        return studentService.editStudentDetails(studentDetails, studentId);
    }

    @PostMapping
    public StudentResponse enrollStudent(@RequestBody StudentRequest studentRequest){
        return studentService.enrollStudent(studentRequest);
    }

    @RolesAllowed({"ROLE_STUDENT"})
    @GetMapping("/{student-id}")
    public StudentResponse retrieveOwnStudentDetails(@PathVariable("student-id") Long studentId, HttpServletRequest request){
        log.info("Retrieving Student with ID"+studentId);
        if (!studentId.equals(request.getAttribute("user-id")))
            throw new ForbiddenRequestException("Sorry you can't view Another Student Details");
        return studentService.retrieveStudent(studentId);
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/admin/{student-id}")
    public StudentResponse retrieveStudent(@PathVariable("student-id") Long studentId){
        return studentService.retrieveStudent(studentId);
    }



    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping
    public ResponseList<StudentResponse> retrieveAllStudents(@ModelAttribute StudentRequestParam studentRequestParam){
        return studentService.retrieveAllStudent(studentRequestParam);
    }


}
