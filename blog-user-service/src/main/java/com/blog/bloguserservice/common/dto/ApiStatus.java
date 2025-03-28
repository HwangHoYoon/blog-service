package com.blog.bloguserservice.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiStatus {
    // 성공 응답 (2xx)
    SUCCESS(200, "요청이 성공했습니다."),
    LOGIN_SUCCESS(200, "로그인 성공"),
    LOGOUT_SUCCESS(200, "로그아웃 성공"),
    REGISTER_SUCCESS(200, "회원가입 성공"),
    REFRESH_SUCCESS(200, "토큰재발급 성공"),
    // 클라이언트 오류 (4xx)
    BAD_REQUEST(400, "잘못된 요청입니다."),
    UNAUTHORIZED(401, "인증이 필요합니다."),
    FORBIDDEN(403, "접근 권한이 없습니다."),
    USER_NOT_FOUND(404, "유저 정보를 찾을 수 없습니다."),
    INVALID_CREDENTIALS(401, "아이디 또는 비밀번호가 올바르지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(401, "Refresh Token이 없습니다."),
    INVALID_REFRESH_TOKEN(401, "유효하지 않은 Refresh Token입니다."),
    REFRESH_TOKEN_MISMATCH(401, "Refresh Token이 일치하지 않습니다."),

    // 서버 오류 (5xx)
    SERVER_ERROR(500, "서버 오류가 발생했습니다."),
    ;

    private final int code;
    private final String message;
}
