package com.example.deapseashop.repository;

import com.example.deapseashop.entity.Item;
import com.example.deapseashop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemJpaRepository extends JpaRepository<Item, Long> {

    public Optional<Item> findByName(String name);
}
