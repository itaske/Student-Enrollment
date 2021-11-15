package com.flexisaf.enrollmentservice.services;

import com.flexisaf.enrollmentservice.exceptions.BadRequestException;
import com.flexisaf.enrollmentservice.models.User;
import com.flexisaf.enrollmentservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.time.LocalDate;

import static java.lang.String.format;

@Service
@Slf4j
public class UserService extends BaseService<User, UserRepository, Long> implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private StudentService studentService;

    public UserService(UserRepository repository) {
        super(repository);
    }

    @PostConstruct
    public void init(){
        User adminUser = new User();
        adminUser.setEmail("udochukwupatric@gmail.com");
        adminUser.setLastName("Udochukwu");
        adminUser.setFirstName("Patrick");
        adminUser.setOtherName("Chibuikem");
        adminUser.setPassword("hello");
        adminUser.setGender(User.Gender.M);
        adminUser.setDateOfBirth(LocalDate.of(1997, 3, 2));
        adminUser.setRole(User.Role.ROLE_ADMIN);

        log.info("Saving Admin User .......");
        save(adminUser);
        log.info("Admin User saved successfully");
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (studentService.matchesMatricNumberPattern(username))
            return studentService.retrieveStudentByMatricNumber(username)
                    .orElseThrow(()-> new BadRequestException(String.format("Student with %s, not found", username)));
        return getUserByEmail(username);
    }


    public User getUserByEmail(String email){
        User user = getRepository().findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(format("User with %s, not found", email)));
        return user;
    }


    @Override
    public User save(User model) {
        model.setPassword(passwordEncoder.encode(model.getPassword())); // encode password

        return super.save(model);
    }

}
