package com.example.deapseashop.controller;

import com.example.deapseashop.domain.item.ItemDto;
import com.example.deapseashop.domain.item.ItemEntity;
import com.example.deapseashop.domain.item.ItemService;
import com.example.deapseashop.domain.user.dtos.UserDto;
import com.example.deapseashop.domain.user.services.UserService;
import com.example.deapseashop.utils.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;

    @GetMapping("/")
    public ResponseEntity<HomeResponse> home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) UserDto userDto) {

        List<ItemDto> itemDtoList = null;
        HomeResponse homeResponse;
        // 세션에 회원 데이터가 없으면 모든 상품들
        if (userDto == null) {
            itemDtoList = itemService.findAllWithSellerName();
            homeResponse = new HomeResponse("익명사용자 상품전체조회", userDto, itemDtoList);
        } else {
            // 세션이 유지되면 회원정보 + (회원이 올린 상품을 제외한) 상품들
            itemDtoList = itemService.findAllWithSellerNameNotLoginUser(userDto.getEmail());
            homeResponse = new HomeResponse("접속중인 사용자의 상품을 제외한 상품전체조회", userDto, itemDtoList);
        }

        return new ResponseEntity<>(homeResponse, HttpStatus.OK);
    }

    // TODO 페이지를 파라미터로 받아서 페이징 쿼리 수행하는 서비스, 컨트롤러 만들기

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    private static class HomeResponse {

        private String message;
        private UserDto userDto;
        @NonNull
        private List<ItemDto> itemDtoList;


    }
}
