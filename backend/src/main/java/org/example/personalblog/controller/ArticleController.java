package org.example.personalblog.controller;

import lombok.RequiredArgsConstructor;
import org.example.personalblog.dto.ArticleContentResponse;
import org.example.personalblog.dto.ArticleMetadataResponse;
import org.example.personalblog.dto.ArticleRequest;
import org.example.personalblog.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleMetadataResponse> addArticle(@Validated(ArticleRequest.CreateValidation.class)
                                                                  @RequestPart("metadata") ArticleRequest articleRequest,
                                                              @RequestPart("content") MultipartFile content) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.addArticle(articleRequest, content));
    }

    @GetMapping("/metadata")
    public ResponseEntity<Page<ArticleMetadataResponse>> getArticlesMetadata(Pageable pageable) {
        return ResponseEntity.ok(articleService.getArticlesMetadata(pageable));
    }

    @GetMapping("/content/{id}")
    public ResponseEntity<ArticleContentResponse> getArticleContent(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(articleService.getArticleContent(id));
    }

    @GetMapping("/tags")
    public ResponseEntity<Set<String>> getAllDistinctTags() {
        return ResponseEntity.ok(articleService.getAllDistinctTags());
    }

    @GetMapping("/metadata/tags")
    public ResponseEntity<Page<ArticleMetadataResponse>> getArticlesMetadataByTags(@RequestParam("tags") Set<String> tags,
                                                                                   Pageable pageable) {
        return ResponseEntity.ok(articleService.getArticlesMetadataByTags(tags, pageable));
    }

    @GetMapping("/metadata/title")
    public ResponseEntity<Page<ArticleMetadataResponse>> getArticleMetadataByTitleContains(@RequestParam("title") String title,
                                                                                     Pageable pageable) {
        return ResponseEntity.ok(articleService.getArticleMetadataByTitleContains(title, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleMetadataResponse> updateArticle(@PathVariable("id") UUID id,
                                                                 @RequestPart(name = "metadata", required = false)
                                                                 @Validated(ArticleRequest.UpdateValidation.class)
                                                                 ArticleRequest articleRequest,
                                                                 @RequestPart(name = "content", required = false)
                                                                 MultipartFile content) {
        return ResponseEntity.ok(articleService.updateArticle(id, articleRequest, content));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") UUID id) {
        articleService.deleteArticle(id);

        return ResponseEntity.noContent().build();
    }
}
