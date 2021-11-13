package com.flexisaf.enrollmentservice.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flexisaf.enrollmentservice.models.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
public class StudentRequestParam {

    @ApiModelProperty(value = "First Name ", notes = "Student's First Name")
    private String firstName;

    @ApiModelProperty(value = "Last Name", notes = "Student's Last Name or Family Name/Surname")
    private String lastName;

    @ApiModelProperty(value = "Other Name", notes = "Student's Other name/Middle Name")
    private String otherName;

    @ApiModelProperty(value = "Gender", notes = "Student's Gender")
    private User.Gender gender;

    @ApiModelProperty(value = "Email Address", notes = "Student's Email Address")
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAtFrom;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAtTo;

    private String departmentName;

    public static final int DEFAULT_CURRENT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;
    public static final String DEFAULT_DIRECTION = "ASC";



    private String currentPage = String.valueOf(DEFAULT_CURRENT_PAGE);
    private String size = String.valueOf(DEFAULT_SIZE);
    private String direction = DEFAULT_DIRECTION;
    private List<String> attributes = Arrays.asList("id");


    public void validate(){
        try{
            int currentPage = Integer.parseInt(getCurrentPage());
        }catch (NumberFormatException e){
            setCurrentPage(String.valueOf(DEFAULT_CURRENT_PAGE));
        }

        try{
            int size = Integer.parseInt(getSize());
        }catch (NumberFormatException e){
            setSize(String.valueOf(DEFAULT_SIZE));
        }
    }
}
