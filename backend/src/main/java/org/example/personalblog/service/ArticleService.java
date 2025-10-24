package org.example.personalblog.service;

import lombok.RequiredArgsConstructor;
import org.example.personalblog.dto.ArticleContentResponse;
import org.example.personalblog.dto.ArticleMetadataResponse;
import org.example.personalblog.dto.ArticleRequest;
import org.example.personalblog.entity.ArticleContent;
import org.example.personalblog.entity.ArticleMetadata;
import org.example.personalblog.exception.ArticleContentNotFoundException;
import org.example.personalblog.exception.ArticleMetadataNotFoundException;
import org.example.personalblog.exception.IncorrectContentException;
import org.example.personalblog.exception.ValueAlreadyExistsException;
import org.example.personalblog.mapper.ArticleMapper;
import org.example.personalblog.repository.ArticleContentRepository;
import org.example.personalblog.repository.ArticleMetadataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleMetadataRepository articleMetadataRepository;
    private final ArticleContentRepository articleContentRepository;
    private final ArticleMapper articleMapper;
    private final SubscriptionService subscriptionService;

    @Transactional
    public ArticleMetadataResponse addArticle(ArticleRequest articleRequest, MultipartFile content) {
        if (articleMetadataRepository.existsBySlug((articleRequest.getSlug()))) {
            throw new ValueAlreadyExistsException("Article with such slug "
                    + articleRequest.getSlug() + " already exists");
        }

        ArticleMetadata articleMetadata = articleMapper.toMetadata(articleRequest);
        ArticleContent articleContent = new ArticleContent();

        try(InputStream stream = content.getInputStream()) {
            articleContent.setContent(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
        } catch (IOException ex) {
            throw new IncorrectContentException(ex.getMessage());
        }
        articleMetadata.setCreatedAt(LocalDate.now());

        ArticleMetadata savedArticleMetadata = articleMetadataRepository.save(articleMetadata);
        articleContent.setId(savedArticleMetadata.getId());
        articleContentRepository.save(articleContent);

        subscriptionService.sendMessage("New article: \"" + articleMetadata.getTitle() + "\"",
                """
                        There is a new article in Personal Blog!
                        Go on and check, maybe you will find it useful.
                        """);

        return articleMapper.toMetadataResponse(savedArticleMetadata);
    }

    public Page<ArticleMetadataResponse> getArticlesMetadata(Pageable pageable) {
        return articleMetadataRepository.findAll(pageable)
                .map(articleMapper::toMetadataResponse);
    }

    public ArticleContentResponse getArticleContent(UUID id) {
        return articleMapper.toContentResponse(articleContentRepository.findById(id).orElseThrow(
                () -> new ArticleContentNotFoundException("Article content not found")));
    }

    public Set<String> getAllDistinctTags() {
        return articleMetadataRepository.findAllDistinctTags();
    }

    public Page<ArticleMetadataResponse> getArticlesMetadataByTags(Set<String> tags, Pageable pageable) {
        if (tags == null || tags.isEmpty()) {
            return new PageImpl<>(Collections.emptyList());
        }

        return articleMetadataRepository.findByTags(tags, pageable).map(articleMapper::toMetadataResponse);
    }

    public Page<ArticleMetadataResponse> getArticleMetadataByTitleContains(String title, Pageable pageable) {
        return articleMetadataRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(articleMapper::toMetadataResponse);
    }

    @Transactional
    public ArticleMetadataResponse updateArticle(UUID id, ArticleRequest articleRequest, MultipartFile content) {
        ArticleMetadata articleMetadata = updateArticleMetadata(id, articleRequest);
        updateArticleContent(id, content);

        return articleMapper.toMetadataResponse(articleMetadata);
    }

    @Transactional
    public void deleteArticle(UUID id) {
        articleMetadataRepository.deleteById(id);
        articleContentRepository.deleteById(id);
    }

    private ArticleMetadata updateArticleMetadata(UUID id, ArticleRequest articleRequest) {
        ArticleMetadata articleMetadata = articleMetadataRepository.findById(id).orElseThrow(
                () -> new ArticleMetadataNotFoundException("Article metadata not found"));

        if (articleRequest.getTitle() != null) {
            articleMetadata.setTitle(articleRequest.getTitle());
        }

        if (articleRequest.getSlug() != null) {
            articleMetadata.setSlug(articleRequest.getSlug());
        }

        if (articleRequest.getTags() != null) {
            articleMetadata.setTags(articleRequest.getTags());
        }

        articleMetadata.setUpdatedAt(LocalDate.now());

        return articleMetadataRepository.save(articleMetadata);
    }

    private void updateArticleContent(UUID id, MultipartFile content) {
        if (content != null) {
            ArticleContent articleContent = articleContentRepository.findById(id).orElseThrow(
                    () -> new ArticleContentNotFoundException("Content not found"));

            try {
                articleContent.setContent(new String(content.getBytes(), StandardCharsets.UTF_8));
            } catch (IOException ex) {
                throw new IncorrectContentException(ex.getMessage());
            }

            articleContentRepository.save(articleContent);
        }
    }
}
