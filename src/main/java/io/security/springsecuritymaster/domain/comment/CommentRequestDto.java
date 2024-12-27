package io.security.springsecuritymaster.domain.comment;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CommentRequestDto(
        @NotBlank @Length(max = 400) String content
        ) {
}
