package com.example.deapseashop.service;

import com.example.deapseashop.dto.OrderItemDto;
import com.example.deapseashop.entity.Item;
import com.example.deapseashop.entity.Member;
import com.example.deapseashop.entity.Order;
import com.example.deapseashop.entity.OrderItem;
import com.example.deapseashop.repository.ItemJpaRepository;
import com.example.deapseashop.repository.MemberJpaRepository;
import com.example.deapseashop.repository.OrderItemJpaRepository;
import com.example.deapseashop.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberJpaRepository memberJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final ItemJpaRepository itemJpaRepository;

    @Transactional
    public Long order(Member member, OrderItemDto... orderItemDtos) {

        // 엔티티 조회
        Member findMember = memberJpaRepository.findById(member.getId()).orElseThrow();
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            Item item = itemJpaRepository.findById(orderItemDto.getItemId()).orElseThrow();
            // 주문상품 생성
            OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), orderItemDto.getQuantity());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);

        orderJpaRepository.save(order);
        return order.getId();
    }
}
