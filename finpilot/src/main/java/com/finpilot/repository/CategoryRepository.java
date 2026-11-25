package com.finpilot.repository;

import com.finpilot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser_UserIdOrUserIsNull(Long userId);
    Optional<Category> findByCategoryIdAndUser_UserId(Long categoryId, Long userId);
}