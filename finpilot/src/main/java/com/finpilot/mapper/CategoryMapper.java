package com.finpilot.mapper;

import com.finpilot.dto.request.CategoryRequest;
import com.finpilot.dto.response.CategoryResponse;
import com.finpilot.entity.Category;
import com.finpilot.entity.User;

public class CategoryMapper {

    public static Category toEntity(CategoryRequest request, User user, Category parentCategory) {

        Category category = new Category();

        category.setCategoryName(request.getCategoryName());
        category.setCategoryType(request.getCategoryType());
        category.setIcon(request.getIcon());
        category.setColour(request.getColour());
        category.setParentCategory(parentCategory);
        category.setIsSystem(request.getIsSystem());
        category.setUser(user);
        category.setDisplayOrder(request.getDisplayOrder());

        return category;
    }

    public static CategoryResponse toResponse(Category category) {

        CategoryResponse response = new CategoryResponse();

        response.setCategoryId(category.getCategoryId());
        response.setCategoryName(category.getCategoryName());
        response.setCategoryType(category.getCategoryType());
        response.setIcon(category.getIcon());
        response.setColour(category.getColour());

        response.setParentCategoryId(
                category.getParentCategory() != null ?
                        category.getParentCategory().getCategoryId() : null
        );

        response.setUserId(
                category.getUser() != null ?
                        category.getUser().getUserId() : null
        );

        response.setIsSystem(category.getIsSystem());
        response.setDisplayOrder(category.getDisplayOrder());

        return response;
    }
}