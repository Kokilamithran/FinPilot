package com.finpilot.service.impl;

import com.finpilot.entity.BudgetItem;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.BudgetItemRepository;
import com.finpilot.repository.TransactionRepository;
import com.finpilot.service.BudgetItemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BudgetItemServiceImpl implements BudgetItemService {

    private final BudgetItemRepository budgetItemRepository;
    private final TransactionRepository transactionRepository;

    public BudgetItemServiceImpl(BudgetItemRepository budgetItemRepository, TransactionRepository transactionRepository) {
        this.budgetItemRepository = budgetItemRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public BudgetItem createBudgetItem(BudgetItem item) {
        calculateAndSetSpentAmount(item);
        return budgetItemRepository.save(item);
    }

    @Override
    public List<BudgetItem> getAllBudgetItems(Long userId, boolean isAdmin) {
        List<BudgetItem> items;
        if (isAdmin) {
            items = budgetItemRepository.findAll();
        } else {
            items = budgetItemRepository.findByBudget_User_UserId(userId);
        }
        items.forEach(this::calculateAndSetSpentAmount);
        return items;
    }

    @Override
    public BudgetItem getBudgetItemById(Long id, Long userId, boolean isAdmin) {
        BudgetItem item;
        if (isAdmin) {
            item = budgetItemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Budget Item not found"));
        } else {
            item = budgetItemRepository.findByItemIdAndUserId(id, userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Budget Item not found"));
        }
        calculateAndSetSpentAmount(item);
        return item;
    }

    @Override
    public BudgetItem updateBudgetItem(Long id, BudgetItem item, Long userId, boolean isAdmin) {

        BudgetItem existing = getBudgetItemById(id, userId, isAdmin);

        existing.setBudget(item.getBudget());
        existing.setCategory(item.getCategory());
        existing.setAllocatedAmount(item.getAllocatedAmount());
        if (item.getAlertPercentage() != null) {
            existing.setAlertPercentage(item.getAlertPercentage());
        }
        if (item.getRolloverEnabled() != null) {
            existing.setRolloverEnabled(item.getRolloverEnabled());
        }
        if (item.getRolloverAmount() != null) {
            existing.setRolloverAmount(item.getRolloverAmount());
        }
        if (item.getNotes() != null) {
            existing.setNotes(item.getNotes());
        }

        calculateAndSetSpentAmount(existing);
        return budgetItemRepository.save(existing);
    }

    @Override
    public void deleteBudgetItem(Long id, Long userId, boolean isAdmin) {
        BudgetItem existing = getBudgetItemById(id, userId, isAdmin);
        budgetItemRepository.delete(existing);
    }

    private void calculateAndSetSpentAmount(BudgetItem item) {
        if (item != null && item.getBudget() != null && item.getCategory() != null && item.getBudget().getUser() != null) {
            LocalDate startDate = item.getBudget().getStartDate() != null ? item.getBudget().getStartDate() : LocalDate.of(1970, 1, 1);
            LocalDate endDate = item.getBudget().getEndDate() != null ? item.getBudget().getEndDate() : LocalDate.of(2099, 12, 31);

            BigDecimal spent = transactionRepository.calculateSpentAmount(
                    item.getBudget().getUser().getUserId(),
                    item.getCategory().getCategoryId(),
                    startDate,
                    endDate
            );
            item.setSpentAmount(spent != null ? spent : BigDecimal.ZERO);
        }
    }
}