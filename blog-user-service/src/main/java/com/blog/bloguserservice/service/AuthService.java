package com.blog.bloguserservice.service;

import com.blog.bloguserservice.common.dto.ApiStatus;
import com.blog.bloguserservice.common.exception.LoginFailedException;
import com.blog.bloguserservice.common.exception.TokenException;
import com.blog.bloguserservice.common.utils.JwtProvider;
import com.blog.bloguserservice.dto.UserLoginReqDto;
import com.blog.bloguserservice.entity.User;
import com.blog.bloguserservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public String login(UserLoginReqDto userLoginReqDto, HttpServletResponse response) {
        // 1. 사용자 정보 조회
        User user = userRepository.findByEmail(userLoginReqDto.getEmail());
        if (user == null || !passwordEncoder.matches(userLoginReqDto.getPassword(), user.getPassword())) {
            throw new LoginFailedException(ApiStatus.INVALID_CREDENTIALS.getMessage());
        }

        // 2. Access & Refresh Token 생성
        String userId = String.valueOf(user.getId());
        String accessToken = jwtProvider.generateAccessToken(userId);
        String refreshToken = jwtProvider.generateRefreshToken(userId);

        // 3. Redis에 Refresh Token 저장 (예: 7일)
        refreshTokenService.saveRefreshToken(userId, refreshToken, 7 * 24 * 60 * 60 * 1000L);

        // 4. Refresh Token을 HttpOnly 쿠키에 저장
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true) // XSS 방지
                .secure(true)   // HTTPS에서만 전송
                .path("/")      // 모든 경로에서 사용 가능
                .maxAge(Duration.ofDays(7)) // 7일간 유효
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // 5. Access Token을 리턴
        return accessToken;
    }

    public String refreshToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            throw new TokenException(ApiStatus.REFRESH_TOKEN_NOT_FOUND.getMessage());
        }

        // 1. Refresh Token 검증 및 유저 정보 가져오기
        String userId = jwtProvider.validateAndGetUserId(refreshToken);
        if (userId == null) {
            throw new TokenException(ApiStatus.INVALID_REFRESH_TOKEN.getMessage());
        }

        // 2. Redis에서 Refresh Token 확인
        String storedToken = refreshTokenService.getRefreshToken(userId);
        if (!refreshToken.equals(storedToken)) {
            throw new TokenException(ApiStatus.REFRESH_TOKEN_MISMATCH.getMessage());
        }

        // 3. 새로운 Access Token 발급
        String newAccessToken = jwtProvider.generateAccessToken(userId);

        // 4. 새로운 Refresh Token 발급
        String newRefreshToken = jwtProvider.generateRefreshToken(userId);

        // 5. 기존 Refresh Token 삭제 (Redis 또는 DB)
        refreshTokenService.deleteRefreshToken(userId);

        // 6. 새로운 Refresh Token 저장
        refreshTokenService.saveRefreshToken(userId, newRefreshToken, refreshTokenExpiration);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return newAccessToken;
    }

    public void logout(String token, HttpServletResponse response) {
        if (token != null) {
            // Redis에서 Refresh Token 삭제
            refreshTokenService.deleteRefreshToken(token);
        }
        // 쿠키 삭제
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // 즉시 삭제
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
    }
}
