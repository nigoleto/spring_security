package io.security.springsecuritymaster.domain.clothes;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClothesResponseDto {

    private Long id;
    private String nickname;
//    private Attach filePath;
//    private String thumbnailUrl;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private int viewCount;

    public static ClothesResponseDto toDto(Clothes clothes) {
        return ClothesResponseDto.builder()
                .id(clothes.getId())
                .nickname(clothes.getUser().getNickname())
                .title(clothes.getTitle())
                .description(clothes.getDescription())
                .createdAt(clothes.getCreatedAt())
                .viewCount(clothes.getViewCount())
                .build();
    }
}
