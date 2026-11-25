package com.finpilot.service;

import com.finpilot.entity.Budget;
import com.finpilot.entity.BudgetItem;
import com.finpilot.entity.Category;
import com.finpilot.entity.User;
import com.finpilot.repository.BudgetItemRepository;
import com.finpilot.repository.TransactionRepository;
import com.finpilot.service.impl.BudgetItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetItemServiceImplTest {

    @Mock
    private BudgetItemRepository budgetItemRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BudgetItemServiceImpl budgetItemService;

    private BudgetItem existingItem;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserId(10L);

        Budget budget = new Budget();
        budget.setBudgetId(14L);
        budget.setUser(user);

        Category category = new Category();
        category.setCategoryId(18L);

        existingItem = new BudgetItem();
        existingItem.setItemId(100L);
        existingItem.setBudget(budget);
        existingItem.setCategory(category);
        existingItem.setAllocatedAmount(new BigDecimal("5000.00"));
        existingItem.setAlertPercentage(85);
        existingItem.setRolloverEnabled(true);
        existingItem.setRolloverAmount(new BigDecimal("500.00"));
        existingItem.setNotes("Existing Notes");
    }

    @Test
    void updateBudgetItem_whenOptionalFieldsOmitted_preservesExistingValues() {
        when(budgetItemRepository.findByItemIdAndUserId(100L, 10L))
                .thenReturn(Optional.of(existingItem));
        when(budgetItemRepository.save(any(BudgetItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BudgetItem incomingUpdate = new BudgetItem();
        incomingUpdate.setBudget(existingItem.getBudget());
        incomingUpdate.setCategory(existingItem.getCategory());
        incomingUpdate.setAllocatedAmount(new BigDecimal("8000.00"));
        incomingUpdate.setAlertPercentage(null);
        incomingUpdate.setRolloverEnabled(null);
        incomingUpdate.setRolloverAmount(null);
        // alertPercentage, rolloverEnabled, rolloverAmount are null (omitted in DTO)

        BudgetItem updated = budgetItemService.updateBudgetItem(100L, incomingUpdate, 10L, false);

        assertEquals(new BigDecimal("8000.00"), updated.getAllocatedAmount());
        assertEquals(85, updated.getAlertPercentage()); // preserved
        assertTrue(updated.getRolloverEnabled()); // preserved
        assertEquals(new BigDecimal("500.00"), updated.getRolloverAmount()); // preserved
        assertEquals("Existing Notes", updated.getNotes()); // preserved
    }

    @Test
    void updateBudgetItem_whenExplicitOptionalFieldsProvided_updatesValues() {
        when(budgetItemRepository.findByItemIdAndUserId(100L, 10L))
                .thenReturn(Optional.of(existingItem));
        when(budgetItemRepository.save(any(BudgetItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BudgetItem incomingUpdate = new BudgetItem();
        incomingUpdate.setBudget(existingItem.getBudget());
        incomingUpdate.setCategory(existingItem.getCategory());
        incomingUpdate.setAllocatedAmount(new BigDecimal("9000.00"));
        incomingUpdate.setAlertPercentage(90);
        incomingUpdate.setRolloverEnabled(false);
        incomingUpdate.setNotes("New Notes");

        BudgetItem updated = budgetItemService.updateBudgetItem(100L, incomingUpdate, 10L, false);

        assertEquals(new BigDecimal("9000.00"), updated.getAllocatedAmount());
        assertEquals(90, updated.getAlertPercentage());
        assertFalse(updated.getRolloverEnabled());
        assertEquals("New Notes", updated.getNotes());
    }
}
