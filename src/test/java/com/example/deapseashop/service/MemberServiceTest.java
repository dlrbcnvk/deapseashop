package com.example.deapseashop.service;

import com.example.deapseashop.entity.Member;
import com.example.deapseashop.repository.MemberJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
public class MemberServiceTest {

    @Autowired
    public MemberService memberService;

    @Autowired
    public MemberJpaRepository memberJpaRepository;

    @Test
    @Rollback
    void 회원가입() {

        //given
        Member member = createMember();
        memberService.join(member);

        //when
        Member findMember = memberJpaRepository.findByEmail(member.getEmail());

        assertThat(findMember).isNotNull();
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
    }

    private Member createMember() {
        String name = "test";
        String email = "test@gmail.com";
        String password = "pass";
        return new Member(name, email, password);
    }
}
