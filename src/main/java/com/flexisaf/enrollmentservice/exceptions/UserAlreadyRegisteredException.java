package com.flexisaf.enrollmentservice.exceptions;

public class UserAlreadyRegisteredException extends RuntimeException{
    public UserAlreadyRegisteredException(String message){
        super(message);
    }
}
