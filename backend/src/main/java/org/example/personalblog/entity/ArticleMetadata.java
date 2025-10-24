package org.example.personalblog.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "articles_metadata")
public class ArticleMetadata {

    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @ElementCollection
    @CollectionTable(name = "article_tags", joinColumns = @JoinColumn(name = "article_metadata_id"))
    @Column(name = "tag")
    private Set<String> tags;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
