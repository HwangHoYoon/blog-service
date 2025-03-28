package com.blog.bloguserservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResDto {
    private Long id;
    private String username;
    private String email;
}
