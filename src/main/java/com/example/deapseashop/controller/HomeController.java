package com.example.deapseashop.controller;

import com.example.deapseashop.domain.user.dtos.UserDto;
import com.example.deapseashop.utils.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) UserDto userDto) {

        // 세션에 회원 데이터가 없으면 상품데이터
        if (userDto == null) {

        }

        // 세션이 유지되면 회원정보 + 상품데이터
        return "";
    }
}
