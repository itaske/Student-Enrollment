package com.flexisaf.enrollmentservice.dto.responses;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.flexisaf.enrollmentservice.models.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class StudentResponse {

    private Long id;

    @ApiModelProperty(value = "First Name ", notes = "Student's First Name")
    private String firstName;

    @ApiModelProperty(value = "Last Name", notes = "Student's Last Name or Family Name/Surname")
    private String lastName;

    @ApiModelProperty(value = "Other Name", notes = "Student's Other name/Middle Name")
    private String otherName;

    @ApiModelProperty(value = "Gender", notes = "Student's Gender")
    private User.Gender gender;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "Date of Birth", notes =  "Student's Date of Birth in format yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @ApiModelProperty(value = "Email Address", notes = "Student's Email Address")
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String matricNumber;

}
