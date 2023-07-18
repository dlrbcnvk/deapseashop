package com.example.deapseashop.repository;

import com.example.deapseashop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    public Member findByEmail(String email);
}
