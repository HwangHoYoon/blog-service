package com.blog.bloguserservice.controller;

import com.blog.bloguserservice.dto.UserResDto;
import com.blog.bloguserservice.entity.User;
import com.blog.bloguserservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResDto> getUserInfo(@AuthenticationPrincipal UserDetails tokenUser) {
        // 1. 사용자 정보 조회
        User user = userService.findByUserId(tokenUser.getUsername());
        UserResDto userResDto = UserResDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        // 2. 사용자 정보 응답
        return ResponseEntity.ok(userResDto);
    }
}
