package io.security.springsecuritymaster.domain.clothes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ClothesRequestDto (
        @NotBlank @Length(max = 30) String title,
        @NotBlank String description,
        @NotNull Long binId,
        Gender gender,
        String size,
        String status
) {
}
