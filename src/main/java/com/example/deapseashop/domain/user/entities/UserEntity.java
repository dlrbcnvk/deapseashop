package com.example.deapseashop.domain.user.entities;

import com.example.deapseashop.domain.item.ItemEntity;
import com.example.deapseashop.domain.user.enums.UserRole;
import com.example.deapseashop.domain.global.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * business key: email
     */
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String username;

    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<ItemEntity> sellingItems = new ArrayList<>();

    public UserEntity() { }

    public UserEntity(String email, String password, String username, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.userRole = userRole;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
