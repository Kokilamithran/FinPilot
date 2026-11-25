package com.finpilot.service.impl;

import com.finpilot.entity.Budget;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.BudgetItemRepository;
import com.finpilot.repository.BudgetRepository;
import com.finpilot.service.BudgetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetItemRepository budgetItemRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository, BudgetItemRepository budgetItemRepository) {
        this.budgetRepository = budgetRepository;
        this.budgetItemRepository = budgetItemRepository;
    }

    @Override
    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public List<Budget> getAllBudgets(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return budgetRepository.findAll();
        }
        return budgetRepository.findByUser_UserId(userId);
    }

    @Override
    public Budget getBudgetById(Long budgetId, Long userId, boolean isAdmin) {
        if (isAdmin) {
            return budgetRepository.findById(budgetId)
                    .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        }
        return budgetRepository.findByBudgetIdAndUser_UserId(budgetId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
    }

    @Override
    public Budget updateBudget(Long budgetId, Budget budget, Long userId, boolean isAdmin) {

        Budget existing = getBudgetById(budgetId, userId, isAdmin);

        existing.setBudgetName(budget.getBudgetName());
        existing.setPeriodType(budget.getPeriodType());
        existing.setStartDate(budget.getStartDate());
        existing.setEndDate(budget.getEndDate());
        existing.setTotalAmount(budget.getTotalAmount());
        existing.setCurrency(budget.getCurrency());
        existing.setIsActive(budget.getIsActive());

        return budgetRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteBudget(Long budgetId, Long userId, boolean isAdmin) {
        Budget existing = getBudgetById(budgetId, userId, isAdmin);
        budgetItemRepository.deleteByBudget_BudgetId(budgetId);
        budgetRepository.delete(existing);
    }
}