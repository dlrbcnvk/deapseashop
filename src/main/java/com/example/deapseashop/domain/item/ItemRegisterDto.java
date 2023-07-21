package com.example.deapseashop.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemRegisterDto {

    private String itemName;
    private int price;
    private int quantity;
}
