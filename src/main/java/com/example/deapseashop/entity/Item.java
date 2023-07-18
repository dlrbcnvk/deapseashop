package com.example.deapseashop.entity;

import com.example.deapseashop.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "item_name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "stock")
    private int stock;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member seller;

    // 기본 생성자
    public Item() { }

    //== 연관관계 편의 메서드 ==//
    private void setSeller(Member seller) {
        // 기존 판매자와의 관계를 제거
        if (this.seller != null) {
            this.seller.getSellingItems().remove(this);
        }
        this.seller = seller;
        seller.getSellingItems().add(this);
    }

    // 생성자
    public Item(String name, int price, int stock, Member seller) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        setSeller(seller);
    }

    public void decreaseStock(int quantity) {
        int restStock = this.stock - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("재고 부족");
        }
    }
}
