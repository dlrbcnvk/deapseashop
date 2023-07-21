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
         * 이메일 중복되어서 회원가입 실패하는 경우 서비스로부터 DuplicateEmailException 받음
         * @ExceptionHandler 통해서 json 으로 던짐
         */
        userService.join(joinRequest);

        /**
         * 회원가입 시에는 세션 유지할 필요 없을 수도 있는데
         * 처음부터 세션 있다고 가정.
         * 회원가입 하기 전에 익명사용자로서 검색을 해서 검색기록이 세션에 쌓였다고 가정.
         */
//        session.invalidate();

        return "회원가입 성공!";
    }
}
