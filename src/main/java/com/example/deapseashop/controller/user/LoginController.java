package com.example.deapseashop.controller.user;

import com.example.deapseashop.domain.user.dtos.UserDto;
import com.example.deapseashop.domain.user.dtos.UserLoginRequest;
import com.example.deapseashop.domain.user.enums.UserRole;
import com.example.deapseashop.domain.user.services.LoginService;
import com.example.deapseashop.exceptions.InvalidEmailException;
import com.example.deapseashop.exceptions.InvalidPasswordException;
import com.example.deapseashop.utils.CookieUtils;
import com.example.deapseashop.utils.SessionConst;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.deapseashop.utils.SessionConst.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(UserLoginRequest userLoginRequest, HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        LoginResponse loginResponse;

        UserDto userDto = null;
        try {
            userDto = loginService.login(userLoginRequest);
        } catch (InvalidEmailException e) {

        } catch (InvalidPasswordException e) {

        }

        if (userDto == null) {
            // email, password 에 맞는 정보가 없을 때
            loginResponse = LoginResponse.FAIL;
            responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
        } else {
            // 성공 시 세션에 userDto 저장
            session.setAttribute(LOGIN_MEMBER, userDto);
            loginResponse = LoginResponse.success(userDto);
            responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }

        return responseEntity;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession();
        // 세션 지우기
        session.removeAttribute(LOGIN_MEMBER);
        session.invalidate();

        return "로그아웃 성공";
    }

    @Getter
    @AllArgsConstructor
    @RequiredArgsConstructor
    private static class LoginResponse {
        enum LoginStatus {
            SUCCESS, FAIL, DELETED
        }

        @NonNull
        private LoginStatus result;
        private String message;
        private UserDto userDto;

        private static String SUCCESS_MESSAGE = "로그인 성공!😆";
        private static String DELETED_MESSAGE = "탈퇴한 계정..😭";

        private static final LoginResponse FAIL = new LoginResponse(LoginStatus.FAIL);

        private static LoginResponse fail(String message) {
            return new LoginResponse(LoginStatus.FAIL, message, null);
        }
        // success 의 경우 userDto 의 값을 set 해줘야 하므로 인스턴스 생성
        private static LoginResponse success(UserDto userDto) {
            return new LoginResponse(LoginStatus.SUCCESS, SUCCESS_MESSAGE, userDto);
        }

        private static LoginResponse deleted(UserDto userDto) {
            return new LoginResponse(LoginStatus.DELETED, DELETED_MESSAGE, userDto);
        }
    }

}
