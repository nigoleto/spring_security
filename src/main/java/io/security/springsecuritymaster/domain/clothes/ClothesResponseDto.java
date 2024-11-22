package io.security.springsecuritymaster.domain.clothes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClothesResponseDto {

    private String nickname;
//    private Attach filePath;
//    private String thumbnailUrl;
    private String title;
    private String description;
    private int viewCount;

    public static ClothesResponseDto toDto(Clothes clothes) {
        return ClothesResponseDto.builder()
                .nickname(clothes.getUser().getNickname())
                .title(clothes.getTitle())
                .description(clothes.getDescription())
                .viewCount(clothes.getViewCount())
                .build();
    }
}
