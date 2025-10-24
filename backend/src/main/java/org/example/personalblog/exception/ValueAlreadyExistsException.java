package org.example.personalblog.exception;

public class ValueAlreadyExistsException extends RuntimeException {
    public ValueAlreadyExistsException(String message) {
        super(message);
    }
}
