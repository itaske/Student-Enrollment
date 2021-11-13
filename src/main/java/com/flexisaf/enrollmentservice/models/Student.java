package com.flexisaf.enrollmentservice.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "students")
@Getter
@Setter
public class Student extends User{


    private String matricNumber;

    public Student(){
        setRole(Role.ROLE_STUDENT);
    }


    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;


}
