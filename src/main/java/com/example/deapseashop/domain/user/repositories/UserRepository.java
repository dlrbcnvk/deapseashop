package com.example.deapseashop.domain.user.repositories;

import com.example.deapseashop.domain.item.ItemEntity;
import com.example.deapseashop.domain.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByEmailOrUsername(String email, String username);

    void deleteByEmail(String email);
}
