package io.security.springsecuritymaster.service;

import io.security.springsecuritymaster.common.exception.BusinessException;
import io.security.springsecuritymaster.common.exception.ErrorCode;
import io.security.springsecuritymaster.domain.attach.Attach;
import io.security.springsecuritymaster.domain.clothes.Clothes;
import io.security.springsecuritymaster.domain.clothes.ClothesRequestDto;
import io.security.springsecuritymaster.domain.clothes.ClothesResponseDto;
import io.security.springsecuritymaster.domain.pub_data.Gwangju;
import io.security.springsecuritymaster.domain.user.User;
import io.security.springsecuritymaster.jwt.JwtUtil;
import io.security.springsecuritymaster.repository.AttachRepository;
import io.security.springsecuritymaster.repository.ClothesRepository;
import io.security.springsecuritymaster.repository.UserRepository;
import io.security.springsecuritymaster.repository.pub_data.GwangjuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClothesService {

    private final ClothesRepository clothesRepository;
    private final UserRepository userRepository;
    private final AttachRepository attachRepository;
    private final GwangjuRepository gwangjuRepository;
    private final S3UploadService s3UploadService;
    private final JwtUtil jwtUtil;

    /**
     * 의류 등록
     **/
    @Transactional
    public void addClothes(String authHeader, ClothesRequestDto clothesRequestDto, MultipartFile attach) throws IOException {

        // 헤더에서 이메일 추출
        String token = authHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);

        // 이메일로 유저 검색
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        // 주소명으로 gwangju 인스턴스 검색
        Optional<Gwangju> OpGwangju = gwangjuRepository.findById(clothesRequestDto.binId());
        Gwangju gwangju = OpGwangju.orElseGet(Gwangju::new);

        Clothes clothes = Clothes.builder()
                .user(user)
                .gwangju(gwangju)
                .title(clothesRequestDto.title())
                .description(clothesRequestDto.description())
                .gender(clothesRequestDto.gender())
                .size(clothesRequestDto.size())
                .status(clothesRequestDto.status())
                .build();


        if (attach != null && !attach.isEmpty()) {
            Attach newAttach = Attach.builder()
                    .fileUrl(s3UploadService.saveFile(attach))
                    .fileName(attach.getOriginalFilename())
                    .fileType(attach.getContentType())
                    .clothes(clothesRepository.save(clothes))
                    .build();
            attachRepository.save(newAttach);
        } else {
            clothesRepository.save(clothes);
        }
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

    /**
     * 의류 정보 수정
     */
    @Transactional
    public void updateClothes(Long id, ClothesRequestDto clothesRequestDto, MultipartFile attach) throws IOException {
        Clothes clothes = clothesRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLOTHES_NOT_FOUND)
        );

        clothes.update(
                clothesRequestDto.title(),
                clothesRequestDto.description()
        );

        if (attach != null && !attach.isEmpty()) {
            Attach newAttach = Attach.builder()
                    .fileUrl(s3UploadService.saveFile(attach))
                    .fileName(attach.getOriginalFilename())
                    .fileType(attach.getContentType())
                    .clothes(clothesRepository.save(clothes))
                    .build();
            attachRepository.save(newAttach);
        } else {
            clothesRepository.save(clothes);
        }

        ClothesResponseDto.toDto(clothes);
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
                .title(clothesRequestDto.title())
                .description(clothesRequestDto.description())
                .build();
    }
}
