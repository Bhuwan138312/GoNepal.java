package com.example.gonepal;

public class NameTooShortException extends Exception {
    public NameTooShortException(String message) {
        super(message);
    }
}