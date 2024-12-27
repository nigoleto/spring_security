package io.security.springsecuritymaster.domain.comment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {

    private Long Id;
    private String email;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .Id(comment.getId())
                .email(comment.getUser().getEmail())
                .nickname(comment.getUser().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
