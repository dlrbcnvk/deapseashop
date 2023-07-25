package com.example.deapseashop.domain.item;

import com.example.deapseashop.domain.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    public Optional<ItemEntity> findByItemNameAndSeller(String itemName, UserEntity seller);

    public List<ItemEntity> findBySeller(UserEntity seller);

    @Query("select i from ItemEntity i join fetch i.seller u " +
            "where i.itemName = :itemName and u.email = :email")
    public Optional<ItemEntity> findByItemNameAndSellerEmail(
            @Param("itemName") String itemName, @Param("email") String email);

    public List<ItemEntity> findBySellerNot(UserEntity seller);

    @Query("select i from ItemEntity i join fetch i.seller u " +
            "where i.seller = u")
    public List<ItemEntity> findAllWithSeller();

    @Query("select i from ItemEntity i join fetch i.seller u " +
            "where i.seller = u and u.email != :email")
    public List<ItemEntity> findAllWithSellerNotLoginUser(@Param("email") String email);


    // TODO seller 연관 삭제 쿼리 짜야 함

    public void deleteAllBySeller(UserEntity user);

    /**
     * 논리 삭제 (update 쿼리, deleted_at=now())
     */
    @Modifying
    @Query("update ItemEntity i set i.deletedAt = now() where i.id in :ids")
    void deleteBySoftDeleteSetDeletedAt(List<Long> ids);

    /**
     * 물리 삭제 (delete 쿼리)
     */
    @Modifying
    @Query("delete from ItemEntity i where i.id in :ids")
    void deleteByHardDelete(List<Long> ids);
}
