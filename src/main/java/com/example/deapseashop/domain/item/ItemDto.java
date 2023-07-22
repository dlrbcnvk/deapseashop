package com.example.deapseashop.domain.item;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemDto {
    private Long id;
    private String itemName;
    private int price;
    private int quantity;
    private String sellerName;
    private LocalDateTime updatedAt;

    /**
     * 반드시 seller 가 1차캐시에 있는 상태에서 사용해야 함.
     * 안 그러면 itemEntity.getSeller().getUsername() 에서 N+1 문제 발생
     *
     */
    public ItemDto(ItemEntity itemEntity) {
        this.id = itemEntity.getId();
        this.itemName = itemEntity.getItemName();
        this.price = itemEntity.getPrice();
        this.quantity = itemEntity.getQuantity();
        this.sellerName = itemEntity.getSeller().getUsername();
        this.updatedAt = itemEntity.getUpdatedAt();
    }

}
