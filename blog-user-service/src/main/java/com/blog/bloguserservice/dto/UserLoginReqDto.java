package com.blog.bloguserservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginReqDto {
    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;
}
