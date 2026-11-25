package com.finpilot.repository;

import com.finpilot.entity.FinancialGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
    List<FinancialGoal> findByUser_UserId(Long userId);
    Optional<FinancialGoal> findByGoalIdAndUser_UserId(Long goalId, Long userId);
}