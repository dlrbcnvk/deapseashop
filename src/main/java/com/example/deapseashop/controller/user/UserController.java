package com.example.deapseashop.controller.user;

import com.example.deapseashop.domain.user.dtos.UserJoinRequest;
import com.example.deapseashop.domain.user.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(UserJoinRequest joinRequest) {
        /**
         * 이메일 또는 이름이 중복되어서 회원가입 실패하는 경우 서비스로부터 RuntimeException 받음
         * 이메일, 이름 모두 중복 -> DuplicateEmailAndUsernameException
         * 이메일 중복 -> DuplicateEmailException
         * 이름 중복 -> DuplicateUsernameException
         * @ExceptionHandler 통해서 json 으로 정상 응답
         */
        userService.join(joinRequest);

        return "회원가입 성공!";
    }
}
