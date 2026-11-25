package com.finpilot.service;

import com.finpilot.entity.BudgetItem;

import java.util.List;

public interface BudgetItemService {

    BudgetItem createBudgetItem(BudgetItem item);

    List<BudgetItem> getAllBudgetItems(Long userId, boolean isAdmin);

    BudgetItem getBudgetItemById(Long id, Long userId, boolean isAdmin);

    BudgetItem updateBudgetItem(Long id, BudgetItem item, Long userId, boolean isAdmin);

    void deleteBudgetItem(Long id, Long userId, boolean isAdmin);
}