package com.example.demo5.handler;

import com.example.demo5.exceptions.DomainException;
import com.example.demo5.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HttpHandler {

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(Exception ex){
        ErrorResponse validationResponse = new ErrorResponse(ex.getMessage());

//        return ResponseEntity.badRequest().body(validationResponse);
        return new ResponseEntity<>(validationResponse, HttpStatus.BAD_REQUEST);
    }
}
