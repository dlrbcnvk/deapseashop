package com.example.deapseashop.service;

import com.example.deapseashop.entity.Item;
import com.example.deapseashop.entity.Member;
import com.example.deapseashop.repository.MemberJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class ItemServiceTest {

    @Autowired
    public MemberService memberService;

    @Autowired
    public ItemService itemService;

    @Autowired
    public MemberJpaRepository memberJpaRepository;

    @Test
    @Rollback
    @Transactional
    void 상품등록() {

        //given
        Member member = createMember();
        memberService.join(member);

        Item item = createItem(member);
        itemService.register(item);

        //when
        Item findItem = itemService.findByName(item.getName());

        // then
        assertThat(item.getName()).isEqualTo(findItem.getName());
        assertThat(item.getPrice()).isEqualTo(findItem.getPrice());
        assertThat(item.getStock()).isEqualTo(findItem.getStock());
        assertThat(item.getSeller()).isNotNull();
        assertThat(item.getSeller().getEmail()).isEqualTo(member.getEmail());
    }

    private Member createMember() {
        String name = "test";
        String email = "test@gmail.com";
        String password = "pass";
        return new Member(name, email, password);
    }

    private Item createItem(Member member) {
        String name = "deapsea handbook";
        int price = 10_000;
        int quantity = 100;
        return new Item(name, price, quantity, member);
    }
}
