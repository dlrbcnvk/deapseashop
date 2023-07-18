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

    private int orderPrice;
    private int quantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item")
    private Review review;

    public OrderItem() { }

    public static OrderItem createOrderItem(Item item, int orderPrice, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setInfos(item, orderPrice, quantity);

        item.removeStock(quantity);
        return orderItem;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    private void setInfos(Item item, int orderPrice, int quantity) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
    }
}
