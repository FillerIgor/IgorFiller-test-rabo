package com.rabobank.customerstateprocessor.exception;

public class CustomerStateException extends RuntimeException {

    public CustomerStateException(String message) {
        super(message);
    }

    public CustomerStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
