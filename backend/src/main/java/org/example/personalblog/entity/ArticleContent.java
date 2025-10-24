package org.example.personalblog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "articles_contents")
public class ArticleContent {

    @Id
    @Column(name = "article_metadata_id")
    private UUID id;

    @Column(name = "content")
    private String content;
}
