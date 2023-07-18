package com.example.deapseashop.repository;

import com.example.deapseashop.entity.Member;
import com.example.deapseashop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
