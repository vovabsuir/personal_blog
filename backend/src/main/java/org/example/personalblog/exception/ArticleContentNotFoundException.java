package org.example.personalblog.exception;

public class ArticleContentNotFoundException extends ResourceNotFoundException {
    public ArticleContentNotFoundException(String message) {
        super(message);
    }
}
