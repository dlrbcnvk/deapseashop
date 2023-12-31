package com.example.deapseashop.domain.user.entities;

import com.example.deapseashop.domain.item.ItemEntity;
import com.example.deapseashop.domain.user.enums.UserRole;
import com.example.deapseashop.domain.global.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
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
     * business key: email, username
     * email 도 겹치면 안 되고
     * username 도 겹치면 안 됨
     */
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String username;

    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @Setter
    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
