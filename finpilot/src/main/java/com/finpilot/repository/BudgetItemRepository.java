package com.finpilot.repository;

import com.finpilot.entity.BudgetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BudgetItemRepository extends JpaRepository<BudgetItem, Long> {
    List<BudgetItem> findByBudget_User_UserId(Long userId);

    @Query("SELECT b FROM BudgetItem b WHERE b.itemId = :itemId AND b.budget.user.userId = :userId")
    Optional<BudgetItem> findByItemIdAndUserId(@Param("itemId") Long itemId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM BudgetItem b WHERE b.budget.budgetId = :budgetId")
    void deleteByBudget_BudgetId(@Param("budgetId") Long budgetId);
}