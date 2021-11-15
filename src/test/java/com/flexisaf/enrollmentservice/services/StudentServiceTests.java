package com.flexisaf.enrollmentservice.services;


import com.flexisaf.enrollmentservice.dto.requests.StudentRequest;
import com.flexisaf.enrollmentservice.dto.responses.StudentResponse;
import com.flexisaf.enrollmentservice.exceptions.BadRequestException;
import com.flexisaf.enrollmentservice.exceptions.ResourceNotFoundException;
import com.flexisaf.enrollmentservice.models.Student;
import com.flexisaf.enrollmentservice.repositories.StudentRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudentServiceTests {

    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void whenValidId_thenStudentShouldBeFound(){

        //Arrange
        Student student = new Student();
        student.setId(2L);
        student.setPassword("password");
        student.setFirstName("Patrick");
        student.setLastName("Udochukwu");
        student.setMatricNumber("FLEXISAF/002");
        student.setOtherName("Chibuikem");
        student.setEmail("udochukwupatric@gmail.com");

        studentService = new StudentService(studentRepository);
        Mockito.when(this.studentRepository.findById(2L)).thenReturn(Optional.of(student));
        Long studentId = 2L;

        //ACT
        StudentResponse found = studentService.retrieveStudent(studentId);


        //Assert
        assertEquals(found.getFirstName(), "Patrick");
        assertEquals(found.getLastName(), "Udochukwu");
        assertEquals(found.getMatricNumber(), "FLEXISAF/002");
    }

    @Test
    public void whenInValidId_thenThrowResourceNotFoundException(){

        //Arrange
        Student student = new Student();
        student.setId(2L);
        student.setPassword("password");
        student.setFirstName("Patrick");
        student.setLastName("Udochukwu");
        student.setMatricNumber("FLEXISAF/002");
        student.setOtherName("Chibuikem");
        student.setEmail("udochukwupatric@gmail.com");

        studentService = new StudentService(studentRepository);
        Mockito.when(this.studentRepository.findById(2L)).thenReturn(Optional.of(student));
        Long studentId = 3L;


        //Act and Assert
        assertThrows(ResourceNotFoundException.class, ()->studentService.retrieveStudent(studentId), "No Resource with ID 3");
    }


//    @Test
//    public void whenStudentEnroll_thenSaveStudent(){
//        //Arrange
//        StudentRequest studentRequest = new StudentRequest();
//        studentRequest.setFirstName("Patrick");
//        studentRequest.setLastName("Udochukwu");
//        studentRequest.setOtherName("Chibuikem");
//        studentRequest.setEmail("udochukwupatric@gmail.com");
//        studentRequest.setDateOfBirth(LocalDate.now().minusYears(19));
//
//        studentService = new StudentService(studentRepository);
//        Student student = studentService.convertFromRequest(studentRequest);
//        student.setId(3L);
//        //Saved Student should have ID
//        Student savedStudent = student;
//
//        Mockito.when(this.studentRepository.save(student)).thenReturn(savedStudent);
//
//
//        //ACT
//        StudentResponse found = studentService.enrollStudent(studentRequest);
//
//
//        //Assert
//        assertEquals(found.getFirstName(), "Patrick");
//        assertEquals(found.getLastName(), "Udochukwu");
//        assertEquals(found.getMatricNumber(), "FLEXISAF/003");
//    }

    @Test
    public void whenStudentEnroll_AndIsLess18_thenThrowException(){
        //Arrange
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("Patrick");
        studentRequest.setLastName("Udochukwu");
        studentRequest.setOtherName("Chibuikem");
        studentRequest.setEmail("udochukwupatric@gmail.com");
        studentRequest.setDateOfBirth(LocalDate.now());

        studentService = new StudentService(studentRepository);
        Student student = studentService.convertFromRequest(studentRequest);

        //Saved Student should have ID
        student.setId(3L);
        Mockito.when(this.studentRepository.save(student)).thenReturn(student);


        //Act and Assert
        assertThrows(BadRequestException.class, ()->studentService.enrollStudent(studentRequest), "Age must be Above or Equal to 18 years");
    }

    @Test
    public void whenStudentEnroll_AndIsGreater25_thenThrowException(){
        //Arrange
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("Patrick");
        studentRequest.setLastName("Udochukwu");
        studentRequest.setOtherName("Chibuikem");
        studentRequest.setEmail("udochukwupatric@gmail.com");
        studentRequest.setDateOfBirth(LocalDate.now().minusYears(30));

        studentService = new StudentService(studentRepository);
        Student student = studentService.convertFromRequest(studentRequest);

        //Saved Student should have ID
        student.setId(3L);
        Mockito.when(this.studentRepository.save(student)).thenReturn(student);


        //Act and Assert
        assertThrows(BadRequestException.class, ()->studentService.enrollStudent(studentRequest), "Age must be Lower or Equal to 25 years");
    }


    @Test
    public void whenStudentDetailsChanges_thenSaveNewStudentDetails(){
        //Arrange
        Student student = new Student();
        student.setId(2L);
        student.setPassword("password");
        student.setFirstName("Patrick");
        student.setLastName("Udochukwu");
        student.setMatricNumber("FLEXISAF/002");
        student.setOtherName("Chibuikem");
        student.setEmail("udochukwupatric@gmail.com");
        Long studentId = 2L;
        studentService = new StudentService(studentRepository);
        Mockito.when(this.studentRepository.existsById(studentId)).thenReturn(true);
        Mockito.when(this.studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Student editedStudent = student;
        editedStudent.setFirstName("Patri");
        Mockito.when(this.studentRepository.saveAndFlush(student)).thenReturn(editedStudent);

        Map<String,Object> studentDetails = new HashMap<>();
        studentDetails.put("firstName", "Patri");

        //ACT
        StudentResponse found = studentService.editStudentDetails(studentDetails,studentId);


        //Assert
        assertEquals(found.getFirstName(), "Patri");
        assertEquals(found.getLastName(), "Udochukwu");
        assertEquals(found.getMatricNumber(), "FLEXISAF/002");
    }

}
