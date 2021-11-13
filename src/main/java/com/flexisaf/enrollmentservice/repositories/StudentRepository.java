package com.flexisaf.enrollmentservice.repositories;

import com.flexisaf.enrollmentservice.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    Optional<Student> findByMatricNumber(String matricNumber);
}