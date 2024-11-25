package io.security.springsecuritymaster.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequestDto(
        @Email @NotBlank String email,
        @NotBlank String password) {
}
