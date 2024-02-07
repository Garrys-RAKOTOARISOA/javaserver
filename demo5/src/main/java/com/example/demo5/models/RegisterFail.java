package com.example.demo5.models;

import lombok.Data;

@Data
public class RegisterFail {
    private boolean success = false;
    private String message;
}
