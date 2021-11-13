package com.flexisaf.enrollmentservice.exceptions;

import com.flexisaf.enrollmentservice.dto.responses.ErrorResponse;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import java.net.UnknownHostException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestHandler(BadRequestException e){
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

   @ExceptionHandler(value = EntityExistsException.class)
   public ResponseEntity<ErrorResponse> entityExist(EntityExistsException e){
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
   }


    @ExceptionHandler(value = {ForbiddenRequestException.class})
    public ResponseEntity<ErrorResponse> forbiddenRequestHandler(ForbiddenRequestException e){
        return buildErrorResponse(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDenied(AccessDeniedException e){
        return buildErrorResponse(e, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Map<String, String> handleValidations(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


    @ExceptionHandler(value = {ResourceNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> resourceNotFoundHandler(Exception e){
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class, UserAlreadyRegisteredException.class})
    public ResponseEntity<ErrorResponse> sqlConstraintViolation(SQLIntegrityConstraintViolationException ex){
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, HttpStatus httpStatus){
        ErrorResponse response = new ErrorResponse();
        response.setCode(httpStatus.value());
        response.setStatus(httpStatus.toString());
        response.setMessage(e.getMessage());
        response.setTimestamp(LocalDateTime.now().toString());
        return new ResponseEntity<>(response, httpStatus);
    }


}
