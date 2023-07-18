package com.example.deapseashop.repository;

import com.example.deapseashop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
}
