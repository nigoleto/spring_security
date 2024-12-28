package io.security.springsecuritymaster.service;

import io.security.springsecuritymaster.common.exception.BusinessException;
import io.security.springsecuritymaster.common.exception.ErrorCode;
import io.security.springsecuritymaster.domain.clothes.Clothes;
import io.security.springsecuritymaster.domain.onmyway.Onmyway;
import io.security.springsecuritymaster.domain.user.User;
import io.security.springsecuritymaster.jwt.JwtUtil;
import io.security.springsecuritymaster.repository.ClothesRepository;
import io.security.springsecuritymaster.repository.OnmywayRepository;
import io.security.springsecuritymaster.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnmywayService {

    private final OnmywayRepository onmywayRepository;
    private final ClothesRepository clothesRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public void addOnmyway(Long clothesId, String authHeader) {
        // 헤더에서 이메일 추출
        String token = authHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);

        // 이메일로 유저 검색
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLOTHES_NOT_FOUND));

        Onmyway onmyway = Onmyway.builder()
                .user(user)
                .clothes(clothes)
                .build();

        if(onmywayRepository.existsByUser_EmailAndClothes_Id(email, clothesId)) {
            Onmyway deletedOnmyway = onmywayRepository.findByUser_EmailAndClothes_Id(email, clothesId);
            onmywayRepository.delete(deletedOnmyway);
        } else onmywayRepository.save(onmyway);
    }

    public long countOnmyway(Long clothesId) {
        return onmywayRepository.countByClothes_Id(clothesId);
    }


    public void deleteOnmyway(Long clothesId, String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);

        Onmyway onmyway = onmywayRepository.findByUser_EmailAndClothes_Id(email, clothesId);

        onmywayRepository.delete(onmyway);
    }
}
