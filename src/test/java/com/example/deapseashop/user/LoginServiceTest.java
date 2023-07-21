package com.example.deapseashop.user;

import com.example.deapseashop.domain.user.dtos.UserDto;
import com.example.deapseashop.domain.user.dtos.UserJoinRequest;
import com.example.deapseashop.domain.user.dtos.UserLoginRequest;
import com.example.deapseashop.domain.user.repositories.UserRepository;
import com.example.deapseashop.domain.user.services.LoginService;
import com.example.deapseashop.domain.user.services.UserService;
import com.example.deapseashop.exceptions.InvalidEmailException;
import com.example.deapseashop.utils.ShaUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class LoginServiceTest {

    private static String EMAIL = "test@aaa.com";
    private static String PASSWORD = "pass";
    private static String USERNAME = "cho";

    @Autowired
    public LoginService loginService;

    @Autowired
    public UserService userService;

    @Autowired
    public UserRepository userRepository;

    @BeforeEach
    void 회원가입() {
        UserJoinRequest userRequest = UserJoinRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME)
                .build();
        userService.join(userRequest);
    }

    @AfterEach
    void 회원삭제() {
        userRepository.deleteAll();
    }

    @Test
    @Rollback
    void 로그인() {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        UserDto userDto = loginService.login(userLoginRequest);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    @Rollback
    void 로그인_실패_잘못된이메일() {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("fake@fake.com")
                .password(PASSWORD)
                .build();

        assertThrows(InvalidEmailException.class, () -> {
            loginService.login(userLoginRequest);
        });
    }
}
