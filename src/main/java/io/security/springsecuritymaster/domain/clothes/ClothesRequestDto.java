package io.security.springsecuritymaster.domain.clothes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ClothesRequestDto (
        @NotBlank @Length(max = 100) String title,
        @NotBlank String description,
        @NotBlank String address
) {
}
