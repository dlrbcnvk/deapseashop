package com.example.deapseashop.domain.user.repositories;

import com.example.deapseashop.domain.item.ItemEntity;
import com.example.deapseashop.domain.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByEmailOrUsername(String email, String username);

    void deleteByEmail(String email);

    void deleteById(Long id);

    /**
     * 논리 삭제 (update 쿼리, deleted_at=now())
     */
    @Modifying
    @Query("update UserEntity u set u.deletedAt = now() where u.id = :id")
    void deleteBySetDeletedAt(Long id);
}
