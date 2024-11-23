package io.security.springsecuritymaster.service;

import io.security.springsecuritymaster.common.exception.BusinessException;
import io.security.springsecuritymaster.common.exception.ErrorCode;
import io.security.springsecuritymaster.domain.clothes.Clothes;
import io.security.springsecuritymaster.domain.clothes.ClothesRequestDto;
import io.security.springsecuritymaster.domain.clothes.ClothesResponseDto;
import io.security.springsecuritymaster.domain.user.User;
import io.security.springsecuritymaster.repository.ClothesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClothesService {

    private final ClothesRepository clothesRepository;

    /** 의류 등록 **/
    @Transactional
    public Clothes addClothes(ClothesRequestDto clothesRequestDto) {
        return clothesRepository.save(convertToEntity(clothesRequestDto));
    }

    /** 전체 의류 조회 */
    @Transactional
    public Page<ClothesResponseDto> getAllClothes(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);

        if(keyword != null && !keyword.isEmpty()) {
            return clothesRepository.findByTitleContainsAndDeletedAtNullOrderByCreatedAtDesc(keyword, pageable)
                    .map(ClothesResponseDto::toDto);
        } else {
            return clothesRepository.findByDeletedAtNullOrderByCreatedAtDesc(pageable)
                    .map(ClothesResponseDto::toDto);
        }
    }

    /** 상세 의류 조회 */
    @Transactional
    public ClothesResponseDto getClothes(Long id) {
        Clothes clothes = clothesRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLOTHES_NOT_FOUND)
        );

        if(clothes.getDeletedAt() != null) {
            throw new BusinessException(ErrorCode.ALREADY_DELETED);
        }
        clothes.increaseViewCount();

        return ClothesResponseDto.toDto(clothes);
    }

    /** 의류 정보 수정 */
    @Transactional
    public ClothesResponseDto updateClothes(Long id, ClothesRequestDto clothesRequestDto) {
        Clothes clothes = clothesRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLOTHES_NOT_FOUND)
        );

        clothes.update(
                clothesRequestDto.title(),
                clothesRequestDto.description()
        );

        return ClothesResponseDto.toDto(clothes);
    }

    /** 의류 삭제 */
    @Transactional
    public void deleteClothes(Long id) {
        Clothes clothes = clothesRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLOTHES_NOT_FOUND)
                );

        clothes.delete();
    }

    /** DTO -> Entity **/
    public Clothes convertToEntity(ClothesRequestDto clothesRequestDto) {
        return Clothes.builder()
                .user(new User(clothesRequestDto.userId()))
                .title(clothesRequestDto.title())
                .description(clothesRequestDto.description())
                .build();
    }
}
