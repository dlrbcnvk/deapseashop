package com.example.deapseashop.dto;

import com.example.deapseashop.entity.Item;
import com.example.deapseashop.entity.Order;
import lombok.Data;

@Data
public class OrderItemDto {

    private Long itemId;
    private int quantity;
}
