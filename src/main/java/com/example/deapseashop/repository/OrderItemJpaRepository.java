package com.example.deapseashop.repository;

import com.example.deapseashop.entity.Order;
import com.example.deapseashop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {
}
