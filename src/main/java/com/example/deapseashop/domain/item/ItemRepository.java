package com.example.deapseashop.domain.item;

import com.example.deapseashop.domain.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    public Optional<ItemEntity> findByItemNameAndSeller(String itemName, UserEntity seller);

    public List<ItemEntity> findBySeller(UserEntity user);

    @Query("select i from ItemEntity i join fetch i.seller u " +
            "where i.itemName = :itemName and u.email = :email")
    public Optional<ItemEntity> findByItemNameAndSellerEmail(
            @Param("itemName") String itemName, @Param("email") String email);
}
