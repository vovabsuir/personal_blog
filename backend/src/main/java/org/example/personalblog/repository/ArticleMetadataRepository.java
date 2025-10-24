package org.example.personalblog.repository;

import org.example.personalblog.entity.ArticleMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ArticleMetadataRepository extends JpaRepository<ArticleMetadata, UUID> {
    @Query("""
        SELECT DISTINCT a FROM ArticleMetadata a
        JOIN a.tags t
        WHERE t IN :tags
    """)
    Page<ArticleMetadata> findByTags(Set<String> tags, Pageable pageable);

    @Query("SELECT DISTINCT tag FROM ArticleMetadata a JOIN a.tags tag")
    Set<String> findAllDistinctTags();

    Page<ArticleMetadata> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    boolean existsBySlug(String slug);
}
