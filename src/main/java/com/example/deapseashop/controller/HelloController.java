package com.example.deapseashop.controller;

import com.example.deapseashop.domain.user.dtos.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HelloController {


    @GetMapping(path = "/setsession")
    public String setsessionAPI(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        Random random = new Random();
        int num = random.nextInt();
        session.setAttribute("KEY",num);
        String returnValue = LocalDateTime.now() +
                " \nsession set id : "+ session.getId() +
                " \nsession set Value : " + num;


        return returnValue;
    }

    @GetMapping(path = "/getsession")
    public String getsessionAPI(HttpServletRequest request){

        HttpSession session = request.getSession(false);

        int num = (int) session.getAttribute("KEY");
        String returnValue = LocalDateTime.now() +
                " \nsession get id :  "+ session.getId() +
                " \nsession get value  " + num;

        return returnValue;
    }

    @GetMapping(path = "/getsessionlogin")
    public String getsessionAfterLogin(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        UserDto userDto = (UserDto) session.getAttribute("LOGIN_SUCCESS");
        String returnValue = LocalDateTime.now() +
                " \nsession get id :  " + session.getId() +
                " \nsession get value  " + userDto;

        return returnValue;
    }
}
