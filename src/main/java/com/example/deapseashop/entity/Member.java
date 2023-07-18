package com.example.deapseashop.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name")
    private String name;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<OrderItemReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<Item> sellingItems = new ArrayList<>();

    public Member() { }

    public Member(String memberName) {
        this.name = memberName;
    }

}
