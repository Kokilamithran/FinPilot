package com.finpilot.service;

import com.finpilot.entity.Budget;

import java.util.List;

public interface BudgetService {

    Budget createBudget(Budget budget);

    List<Budget> getAllBudgets(Long userId, boolean isAdmin);

    Budget getBudgetById(Long budgetId, Long userId, boolean isAdmin);

    Budget updateBudget(Long budgetId, Budget budget, Long userId, boolean isAdmin);

    void deleteBudget(Long budgetId, Long userId, boolean isAdmin);
}