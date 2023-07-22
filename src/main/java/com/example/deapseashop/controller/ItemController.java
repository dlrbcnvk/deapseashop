package com.example.deapseashop.controller;

import com.example.deapseashop.domain.item.ItemEntity;
import com.example.deapseashop.domain.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items")
    public String items() {
        List<ItemEntity> itemList = itemService.findAll();
        return "STOP";
    }


}
