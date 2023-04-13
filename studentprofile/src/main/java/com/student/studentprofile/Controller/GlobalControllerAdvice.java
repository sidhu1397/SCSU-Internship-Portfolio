package com.student.studentprofile.Controller;

import com.student.studentprofile.Domain.ErrorResponse;
import com.student.studentprofile.Exception.StudentProfileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(StudentProfileException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(StudentProfileException e)
    {
        log.error("StudentProfileException Occured",e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setErrorDetail(e.getErrorDetail());
        return new ResponseEntity<>(errorResponse, e.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e)
    {
        log.error("Unknown Exception Occured",e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode("500");
        errorResponse.setErrorDetail(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
