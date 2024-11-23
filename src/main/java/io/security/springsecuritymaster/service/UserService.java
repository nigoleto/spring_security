package io.security.springsecuritymaster.service;

import io.security.springsecuritymaster.common.exception.BusinessException;
import io.security.springsecuritymaster.common.exception.ErrorCode;
import io.security.springsecuritymaster.domain.user.CreateUserRequestDto;
import io.security.springsecuritymaster.domain.user.User;
import io.security.springsecuritymaster.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (user.getDeletedAt() != null) {
            throw new BusinessException(ErrorCode.USER_DEACTIVATED);
        }
        if (!user.isActive()) {
            throw new BusinessException(ErrorCode.USER_SUSPENDED);
        }
        return user;
    }

    @Transactional
    public void createUser(CreateUserRequestDto requestDto) {

        User user = User.builder()
                .email(requestDto.email())
                .password(passwordEncoder.encode(requestDto.password()))
                .nickname(requestDto.nickname())
                .build();

        userRepository.save(user);
    }
}
