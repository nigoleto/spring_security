package io.security.springsecuritymaster.domain.clothes;

import io.security.springsecuritymaster.domain.attach.Attach;
import io.security.springsecuritymaster.domain.pub_data.Gwangju;
import io.security.springsecuritymaster.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ClothesResponseDto {

    private Long id;
    private String nickname;
    private String thumbnailUrl;
    private List<Attach> attachList;
    private User user;
    private Gwangju gwangju;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private int viewCount;

    public static ClothesResponseDto toDto(Clothes clothes) {
        String thumbnailUrl = clothes.getAttachList().isEmpty()
                ? "https://i.pinimg.com/736x/59/83/6c/59836c8f46047eff63997c565f36cb33.jpg"
                : clothes.getAttachList().get(0).getFileUrl();

        return ClothesResponseDto.builder()
                .id(clothes.getId())
                .nickname(clothes.getUser().getNickname())
                .thumbnailUrl(thumbnailUrl)
                .attachList(clothes.getAttachList())
                .user(clothes.getUser())
                .gwangju(clothes.getGwangju())
                .title(clothes.getTitle())
                .description(clothes.getDescription())
                .createdAt(clothes.getCreatedAt())
                .viewCount(clothes.getViewCount())
                .build();
    }
}
