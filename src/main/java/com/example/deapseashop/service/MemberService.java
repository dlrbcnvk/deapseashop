package com.example.deapseashop.service;

import com.example.deapseashop.entity.Member;
import com.example.deapseashop.repository.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;

    public MemberService(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    public Long join(Member member) {
        memberJpaRepository.save(member);
        return member.getId();
    }
}
