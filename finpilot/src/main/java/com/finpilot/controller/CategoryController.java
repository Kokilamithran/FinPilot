package com.finpilot.controller;

import com.finpilot.dto.request.CategoryRequest;
import com.finpilot.dto.response.CategoryResponse;
import com.finpilot.entity.Category;
import com.finpilot.entity.User;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.mapper.CategoryMapper;
import com.finpilot.repository.CategoryRepository;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final SecurityUtils securityUtils;

    public CategoryController(CategoryService categoryService,
                              CategoryRepository categoryRepository,
                              SecurityUtils securityUtils) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        Category parent = null;

        if (request.getParentCategoryId() != null) {
            parent = categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
        }

        Category category = CategoryMapper.toEntity(request, currentUser, parent);

        return CategoryMapper.toResponse(categoryService.createCategory(category));
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return categoryService.getAllCategories(currentUser.getUserId(), isAdmin)
                .stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return CategoryMapper.toResponse(categoryService.getCategoryById(id, currentUser.getUserId(), isAdmin));
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id,
                                          @Valid @RequestBody CategoryRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Category parent = null;

        if (request.getParentCategoryId() != null) {
            parent = categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
        }

        Category category = CategoryMapper.toEntity(request, currentUser, parent);

        return CategoryMapper.toResponse(categoryService.updateCategory(id, category, currentUser.getUserId(), isAdmin));
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        categoryService.deleteCategory(id, currentUser.getUserId(), isAdmin);
        return "Category deleted successfully";
    }
}