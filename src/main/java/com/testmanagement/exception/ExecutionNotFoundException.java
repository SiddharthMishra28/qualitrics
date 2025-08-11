package com.testmanagement.exception;

/**
 * Custom exception for when an execution is not found
 */
public class ExecutionNotFoundException extends RuntimeException {

    public ExecutionNotFoundException(String message) {
        super(message);
    }

    public ExecutionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
