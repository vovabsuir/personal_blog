package org.example.personalblog.exception;

public class ArticleMetadataNotFoundException extends ResourceNotFoundException {
  public ArticleMetadataNotFoundException(String message) {
    super(message);
  }
}
