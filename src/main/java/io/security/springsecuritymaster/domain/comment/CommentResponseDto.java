package io.security.springsecuritymaster.domain.comment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {

    private String nickName;
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .nickName(comment.getUser().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
