package com.example.deapseashop.domain.item;

import com.example.deapseashop.domain.global.BaseEntity;
import com.example.deapseashop.domain.user.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "items")
@EntityListeners(AuditingEntityListener.class)
public class ItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * business key: (itemName, seller)
     */
    private String itemName;
    private int price;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity seller;

    public ItemEntity() { }

    public ItemEntity(String itemName, int price, int quantity, UserEntity seller) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.seller = seller;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateItem(String updateItemName, int updatePrice, int updateQuantity) {
        this.itemName = updateItemName;
        this.price = updatePrice;
        this.quantity = updateQuantity;
    }
}
