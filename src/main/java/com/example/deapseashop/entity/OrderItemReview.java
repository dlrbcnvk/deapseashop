package com.example.deapseashop.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class OrderItemReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviews")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    private OrderItem orderItem;

    private String review;

    public OrderItemReview() { }

    public OrderItemReview(Member member, OrderItem orderItem, String review) {
        this.member = member;
        this.orderItem = orderItem;
        this.review = review;
    }

    public void updateReview(String review) {
        this.review = review;
    }
}
