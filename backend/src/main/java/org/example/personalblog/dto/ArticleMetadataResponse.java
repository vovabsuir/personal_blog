package org.example.personalblog.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class ArticleMetadataResponse {
    private UUID id;
    private String title;
    private String slug;
    private Set<String> tags;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
