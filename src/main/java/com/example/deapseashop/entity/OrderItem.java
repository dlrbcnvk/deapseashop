package com.example.deapseashop.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderItems")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    public OrderItem() { }

    public OrderItem(Order order, Item item) {
        this.order = order;
        this.item = item;
    }
}
