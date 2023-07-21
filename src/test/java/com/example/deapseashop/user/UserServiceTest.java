package com.example.deapseashop.user;

import com.example.deapseashop.domain.user.dtos.UserJoinRequest;
import com.example.deapseashop.domain.user.entities.UserEntity;
import com.example.deapseashop.domain.user.services.UserService;
import com.example.deapseashop.exceptions.DuplicateEmailException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Autowired
    public UserService userService;

    @Test
    @Rollback
    void 회원가입() {
        // given
        UserJoinRequest userRequest = UserJoinRequest.builder()
                .email("test@aaa.com")
                .password("pass")
                .username("cho")
                .build();
        userService.join(userRequest);

        // when
        UserEntity findUser = userService.findByEmail(userRequest.getEmail()).orElseThrow();

        // then
        assertThat(findUser).isNotNull();
        assertThat(findUser.getEmail()).isEqualTo(userRequest.getEmail());

        log.info("password={}", findUser.getPassword());
    }

    @Test
    @Rollback
    void 회원가입_실패_이메일중복() {
        UserJoinRequest userRequest = UserJoinRequest.builder()
                .email("test@aaa.com")
                .password("pass")
                .username("cho")
                .build();
        userService.join(userRequest);

        assertThrows(DuplicateEmailException.class, () -> {
            userService.join(userRequest);
        });
    }
}
