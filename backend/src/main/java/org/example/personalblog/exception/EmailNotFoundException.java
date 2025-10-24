package org.example.personalblog.exception;

public class EmailNotFoundException extends RuntimeException {
  public EmailNotFoundException(String message) {
    super(message);
  }
}
