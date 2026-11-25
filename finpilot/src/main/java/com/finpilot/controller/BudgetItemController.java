package com.finpilot.controller;

import com.finpilot.dto.request.BudgetItemRequest;
import com.finpilot.dto.response.BudgetItemResponse;
import com.finpilot.entity.Budget;
import com.finpilot.entity.BudgetItem;
import com.finpilot.entity.Category;
import com.finpilot.entity.User;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.mapper.BudgetItemMapper;
import com.finpilot.repository.BudgetRepository;
import com.finpilot.repository.CategoryRepository;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.BudgetItemService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/budget-items")
public class BudgetItemController {

    private final BudgetItemService budgetItemService;
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final SecurityUtils securityUtils;

    public BudgetItemController(BudgetItemService budgetItemService,
                                BudgetRepository budgetRepository,
                                CategoryRepository categoryRepository,
                                SecurityUtils securityUtils) {
        this.budgetItemService = budgetItemService;
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public BudgetItemResponse createBudgetItem(@Valid @RequestBody BudgetItemRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Budget budget;
        if (isAdmin) {
            budget = budgetRepository.findById(request.getBudgetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        } else {
            budget = budgetRepository.findByBudgetIdAndUser_UserId(request.getBudgetId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        BudgetItem item = BudgetItemMapper.toEntity(request, budget, category);

        return BudgetItemMapper.toResponse(
                budgetItemService.createBudgetItem(item)
        );
    }

    @GetMapping
    public List<BudgetItemResponse> getAllBudgetItems() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return budgetItemService.getAllBudgetItems(currentUser.getUserId(), isAdmin)
                .stream()
                .map(BudgetItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BudgetItemResponse getBudgetItemById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return BudgetItemMapper.toResponse(
                budgetItemService.getBudgetItemById(id, currentUser.getUserId(), isAdmin)
        );
    }

    @PutMapping("/{id}")
    public BudgetItemResponse updateBudgetItem(
            @PathVariable Long id,
            @Valid @RequestBody BudgetItemRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Budget budget;
        if (isAdmin) {
            budget = budgetRepository.findById(request.getBudgetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        } else {
            budget = budgetRepository.findByBudgetIdAndUser_UserId(request.getBudgetId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        BudgetItem item = BudgetItemMapper.toEntity(
                request,
                budget,
                category
        );

        return BudgetItemMapper.toResponse(
                budgetItemService.updateBudgetItem(id, item, currentUser.getUserId(), isAdmin)
        );
    }

    @DeleteMapping("/{id}")
    public String deleteBudgetItem(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        budgetItemService.deleteBudgetItem(id, currentUser.getUserId(), isAdmin);
        return "Budget Item deleted successfully";
    }
}