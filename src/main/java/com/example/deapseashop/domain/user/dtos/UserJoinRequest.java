package com.example.deapseashop.domain.user.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserJoinRequest {

    @NotBlank(message = "Input your email")
    private String email;
    @NotBlank(message = "Input your password")
    private String password;
    @NotBlank(message = "Input your name")
    private String username;
}
