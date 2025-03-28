package com.blog.bloguserservice.controller;

import com.blog.bloguserservice.common.dto.ApiResponse;
import com.blog.bloguserservice.common.dto.ApiStatus;
import com.blog.bloguserservice.dto.UserLoginReqDto;
import com.blog.bloguserservice.dto.UserReqDto;
import com.blog.bloguserservice.service.AuthService;
import com.blog.bloguserservice.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody @Valid UserReqDto requestDto) {
        userService.register(requestDto);
        return ResponseEntity.ok(ApiResponse.success(ApiStatus.REGISTER_SUCCESS));
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid UserLoginReqDto userLoginReqDto, HttpServletResponse response) {
        String token = authService.login(userLoginReqDto, response);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(ApiResponse.success(ApiStatus.LOGIN_SUCCESS));
    }

    // 토큰 재발급 API
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        String token = authService.refreshToken(refreshToken, response);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(ApiResponse.success(ApiStatus.REFRESH_SUCCESS));
    }

    // 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(@Parameter(hidden = true) @CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        authService.logout(refreshToken, response);
        return ResponseEntity.ok(ApiResponse.success(ApiStatus.LOGOUT_SUCCESS, null));
    }

}
