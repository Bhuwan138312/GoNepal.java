package com.example.gonepal;

public class PasswordTooWeakException extends Exception {
    public PasswordTooWeakException(String message) {
        super(message);
    }
}