package com.finpilot.service;

import com.finpilot.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);

    List<Category> getAllCategories(Long userId, boolean isAdmin);

    Category getCategoryById(Long categoryId, Long userId, boolean isAdmin);

    Category updateCategory(Long categoryId, Category category, Long userId, boolean isAdmin);

    void deleteCategory(Long categoryId, Long userId, boolean isAdmin);
}