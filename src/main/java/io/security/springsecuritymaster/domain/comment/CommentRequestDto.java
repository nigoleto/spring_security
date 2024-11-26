package io.security.springsecuritymaster.domain.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record CommentRequestDto(
        @NotNull Long userId,
        @NotBlank @Length(max = 200) String comment
        ) {
}
