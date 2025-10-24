package org.example.personalblog.repository;

import org.example.personalblog.entity.ArticleContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ArticleContentRepository extends JpaRepository<ArticleContent, UUID> {
}
