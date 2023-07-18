package com.example.deapseashop.service;

import com.example.deapseashop.entity.Item;
import com.example.deapseashop.repository.ItemJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemJpaRepository itemJpaRepository;

    public ItemService(ItemJpaRepository itemJpaRepository) {
        this.itemJpaRepository = itemJpaRepository;
    }

    public Long register(Item item) {
        itemJpaRepository.save(item);
        return item.getId();
    }

    public Item findByName(String itemName) {
        return itemJpaRepository.findByName(itemName).orElseThrow();
    }
}
