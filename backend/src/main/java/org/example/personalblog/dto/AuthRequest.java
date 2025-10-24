package org.example.personalblog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequest {
    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
