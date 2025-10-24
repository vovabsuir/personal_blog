package org.example.personalblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class ArticleRequest {
    public interface CreateValidation extends Default {}
    public interface UpdateValidation extends Default {}

    @NotBlank(groups = CreateValidation.class)
    @Size(max = 255, groups = {CreateValidation.class, UpdateValidation.class})
    private String title;

    @NotBlank(groups = CreateValidation.class)
    @Size(max = 255, groups = {CreateValidation.class, UpdateValidation.class})
    private String slug;

    private Set<String> tags;
}
