package com.example.deapseashop.controller;

import com.example.deapseashop.domain.item.ItemEntity;
import com.example.deapseashop.domain.item.ItemRegisterDto;
import com.example.deapseashop.domain.item.ItemService;
import com.example.deapseashop.domain.user.dtos.UserDto;
import com.example.deapseashop.utils.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/items")
    public ResponseEntity<String> items(ItemRegisterDto itemRegisterDto,
                                        @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) UserDto userDto) {

        if (userDto == null) {

        }

        if (userDto.getEmail() != itemRegisterDto.getSellerEmail()) {

        }
        itemService.register(itemRegisterDto);
        return new ResponseEntity<>("상품 등록 성공!", HttpStatus.OK);
    }
}
