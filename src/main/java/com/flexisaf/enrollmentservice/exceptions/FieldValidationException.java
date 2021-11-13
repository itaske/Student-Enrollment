package com.flexisaf.enrollmentservice.exceptions;


public class FieldValidationException extends RuntimeException {
    public FieldValidationException(String message){
        super(message);
    }
}