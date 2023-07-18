package com.example.deapseashop.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviews")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "review")
    private OrderItem orderItem;

    private String review;

    public Review() { }

    public Review(Member member, OrderItem orderItem, String review) {
        this.member = member;
        this.orderItem = orderItem;
        this.review = review;
    }

    public void updateReview(String review) {
        this.review = review;
    }
}
