package com.finpilot.service.impl;

import com.finpilot.entity.Category;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.CategoryRepository;
import com.finpilot.service.CategoryService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return categoryRepository.findAll();
        }
        return categoryRepository.findByUser_UserIdOrUserIsNull(userId);
    }

    @Override
    public Category getCategoryById(Long categoryId, Long userId, boolean isAdmin) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (isAdmin || category.getUser() == null || category.getUser().getUserId().equals(userId)) {
            return category;
        }

        throw new ResourceNotFoundException("Category not found");
    }

    @Override
    public Category updateCategory(Long categoryId, Category category, Long userId, boolean isAdmin) {
        Category existing = getCategoryById(categoryId, userId, isAdmin);

        if (!isAdmin && (existing.getUser() == null || Boolean.TRUE.equals(existing.getIsSystem()))) {
            throw new AccessDeniedException("System categories are read-only and cannot be modified.");
        }

        existing.setCategoryName(category.getCategoryName());
        existing.setCategoryType(category.getCategoryType());
        existing.setIcon(category.getIcon());
        existing.setColour(category.getColour());
        existing.setParentCategory(category.getParentCategory());
        if (category.getIsSystem() != null) {
            existing.setIsSystem(category.getIsSystem());
        }
        existing.setDisplayOrder(category.getDisplayOrder());

        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategory(Long categoryId, Long userId, boolean isAdmin) {
        Category existing = getCategoryById(categoryId, userId, isAdmin);

        if (!isAdmin && (existing.getUser() == null || Boolean.TRUE.equals(existing.getIsSystem()))) {
            throw new AccessDeniedException("System categories are read-only and cannot be deleted.");
        }

        categoryRepository.delete(existing);
    }
}