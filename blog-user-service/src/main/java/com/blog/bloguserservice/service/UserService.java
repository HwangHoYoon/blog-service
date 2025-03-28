package com.blog.bloguserservice.service;

import com.blog.bloguserservice.common.dto.ApiStatus;
import com.blog.bloguserservice.constant.Role;
import com.blog.bloguserservice.dto.UserReqDto;
import com.blog.bloguserservice.entity.User;
import com.blog.bloguserservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserReqDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = User.builder()
                .username(requestDto.getUsername())
                .password(encodedPassword)
                .email(requestDto.getEmail())
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    public User findByUserId(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new UsernameNotFoundException(ApiStatus.USER_NOT_FOUND.getMessage()));
    }
}
