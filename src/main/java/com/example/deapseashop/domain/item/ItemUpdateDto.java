package com.example.deapseashop.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemUpdateDto {

    private String updateItemName;
    private int updatePrice;
    private int updateQuantity;
}
