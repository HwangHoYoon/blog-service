package com.blog.bloguserservice.common.dto;

/**
 * @param code    상태 코드 (예: 200, 400, 401 등)
 * @param message 응답 메시지 (예: "로그인 성공")
 * @param data    응답 데이터 (유저 정보 등)
 */
public record ApiResponse<T>(int code, String message, T data) {
    // 성공 응답
    public static <T> ApiResponse<T> success(ApiStatus status) {
        return new ApiResponse<>(status.getCode(), status.getMessage(), null);
    }

    public static <T> ApiResponse<T> success(ApiStatus status, T data) {
        return new ApiResponse<>(status.getCode(), status.getMessage(), data);
    }

    // 실패 응답
    public static ApiResponse<?> error(ApiStatus status) {
        return new ApiResponse<>(status.getCode(), status.getMessage(), null);
    }
}
