package com.flexisaf.enrollmentservice.exceptions;

public class ForbiddenRequestException extends RuntimeException{
    public ForbiddenRequestException(String message){
        super(message);
    }
}
