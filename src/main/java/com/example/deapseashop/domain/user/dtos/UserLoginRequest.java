package com.example.deapseashop.domain.user.dtos;

import com.example.deapseashop.utils.ShaUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLoginRequest {
    @NotBlank(message = "Input your email")
    private String email;
    @NotBlank(message = "Input your password")
    private String password;

    public void encryptPwd() {
        password = ShaUtils.encryptSHA256(password);
    }
}
