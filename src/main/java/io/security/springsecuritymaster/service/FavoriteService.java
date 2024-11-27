package io.security.springsecuritymaster.service;

import io.security.springsecuritymaster.common.exception.BusinessException;
import io.security.springsecuritymaster.common.exception.ErrorCode;
import io.security.springsecuritymaster.domain.clothes.Clothes;
import io.security.springsecuritymaster.domain.favorite.Favorite;
import io.security.springsecuritymaster.domain.user.User;
import io.security.springsecuritymaster.jwt.JwtUtil;
import io.security.springsecuritymaster.repository.ClothesRepository;
import io.security.springsecuritymaster.repository.FavoriteRepository;
import io.security.springsecuritymaster.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ClothesRepository clothesRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public void addFavorite(Long clothesId, String authHeader) {

        // 헤더에서 이메일 추출
        String token = authHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);

        // 이메일로 유저 검색
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLOTHES_NOT_FOUND));

        Favorite favorite = Favorite.builder()
                .user(user)
                .clothes(clothes)
                .build();

        favoriteRepository.save(favorite);
    }

    public List<Long> myFavorite(String authHeader) {
        // 헤더에서 이메일 추출
        String token = authHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);

        List<Favorite> favoriteList = favoriteRepository.findByUser_Email(email);

        return favoriteList.stream().map(favorite -> favorite.getClothes().getId()).toList();
    }

    public void deleteFavorite(Long clothesId, String authHeader) {
        // 헤더에서 이메일 추출
        String token = authHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);

        Favorite favorite = favoriteRepository.findByUser_EmailAndClothes_Id(email, clothesId);

        favoriteRepository.delete(favorite);
    }
}
