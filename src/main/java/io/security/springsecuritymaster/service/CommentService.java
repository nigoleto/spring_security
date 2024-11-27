package io.security.springsecuritymaster.service;

import io.security.springsecuritymaster.common.exception.BusinessException;
import io.security.springsecuritymaster.common.exception.ErrorCode;
import io.security.springsecuritymaster.domain.clothes.Clothes;
import io.security.springsecuritymaster.domain.comment.Comment;
import io.security.springsecuritymaster.domain.comment.CommentRequestDto;
import io.security.springsecuritymaster.domain.comment.CommentResponseDto;
import io.security.springsecuritymaster.domain.user.User;
import io.security.springsecuritymaster.jwt.JwtUtil;
import io.security.springsecuritymaster.repository.ClothesRepository;
import io.security.springsecuritymaster.repository.UserRepository;
import io.security.springsecuritymaster.repository.pub_data.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ClothesRepository clothesRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void addComment(Long clothesId, String authHeader, CommentRequestDto commentRequestDto) {
        // clothesId로 clothes 검색
        Clothes clothes = clothesRepository.findById(clothesId).orElseThrow(() ->
            new BusinessException(ErrorCode.CLOTHES_NOT_FOUND)
        );

        // 헤더에서 이메일 추출
        String token = authHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);

        // 이메일로 유저 검색
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Comment newComment = Comment.builder()
                .user(user)
                .clothes(clothes)
                .content(commentRequestDto.content())
                .build();

        commentRepository.save(newComment);
    }

    @Transactional
    public List<CommentResponseDto> getComments(Long clothesId) {
        List<Comment> comments = commentRepository.findByClothesIdAndDeletedAtNullOrderByCreatedAtDesc(clothesId);

        return comments.stream().map(CommentResponseDto::toDto).toList();
    }

    @Transactional
    public void deleteComment(Long commentId, String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        // JWT에서 가져온 Email과 comment ID로 가져온 Email이 같으면 삭제
        if(comment.getUser().getEmail().equals(email)) {
            comment.delete();
        }
    }
}
