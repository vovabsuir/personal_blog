package org.example.personalblog.mapper;

import org.example.personalblog.dto.ArticleContentResponse;
import org.example.personalblog.dto.ArticleMetadataResponse;
import org.example.personalblog.dto.ArticleRequest;
import org.example.personalblog.entity.ArticleContent;
import org.example.personalblog.entity.ArticleMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ArticleMapper {

    ArticleContent toContent(ArticleRequest articleRequest);
    ArticleMetadata toMetadata(ArticleRequest articleRequest);

    ArticleContentResponse toContentResponse(ArticleContent articleContent);
    ArticleMetadataResponse toMetadataResponse(ArticleMetadata articleMetadata);
}
