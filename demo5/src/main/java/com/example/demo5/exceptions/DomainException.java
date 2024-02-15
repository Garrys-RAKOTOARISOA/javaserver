package com.example.demo5.exceptions;

public class DomainException extends RuntimeException{

    public DomainException() {
    }

    public DomainException(String message) {
        super(message);
    }
}
