package com.finpilot.controller;

import com.finpilot.dto.request.BudgetRequest;
import com.finpilot.dto.response.BudgetResponse;
import com.finpilot.entity.Budget;
import com.finpilot.entity.User;
import com.finpilot.mapper.BudgetMapper;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final SecurityUtils securityUtils;

    public BudgetController(BudgetService budgetService,
                            SecurityUtils securityUtils) {
        this.budgetService = budgetService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public BudgetResponse createBudget(@Valid @RequestBody BudgetRequest request) {
        User currentUser = securityUtils.getCurrentUser();

        Budget budget = BudgetMapper.toEntity(request, currentUser);

        return BudgetMapper.toResponse(
                budgetService.createBudget(budget)
        );
    }

    @GetMapping
    public List<BudgetResponse> getAllBudgets() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return budgetService.getAllBudgets(currentUser.getUserId(), isAdmin)
                .stream()
                .map(BudgetMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BudgetResponse getBudgetById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return BudgetMapper.toResponse(
                budgetService.getBudgetById(id, currentUser.getUserId(), isAdmin)
        );
    }

    @PutMapping("/{id}")
    public BudgetResponse updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Budget budget = BudgetMapper.toEntity(request, currentUser);

        return BudgetMapper.toResponse(
                budgetService.updateBudget(id, budget, currentUser.getUserId(), isAdmin)
        );
    }

    @DeleteMapping("/{id}")
    public String deleteBudget(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        budgetService.deleteBudget(id, currentUser.getUserId(), isAdmin);
        return "Budget deleted successfully";
    }
}