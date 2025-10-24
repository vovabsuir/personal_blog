package org.example.personalblog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionRequest {
    @NotBlank
    @Email
    private String email;
}
