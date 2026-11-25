package com.finpilot.repository;

import com.finpilot.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUser_UserId(Long userId);
    Optional<Budget> findByBudgetIdAndUser_UserId(Long budgetId, Long userId);
}