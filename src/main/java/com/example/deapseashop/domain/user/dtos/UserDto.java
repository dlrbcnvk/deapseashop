package com.example.deapseashop.domain.user.dtos;

import com.example.deapseashop.domain.user.enums.UserRole;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {

    private String email;
    private String username;
    private UserRole userRole;

    public UserDto(String email, String username, UserRole userRole) {
        this.email = email;
        this.username = username;
        this.userRole = userRole;
    }
}
