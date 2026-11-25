package com.finpilot.service;

import com.finpilot.entity.FinancialGoal;

import java.util.List;

public interface FinancialGoalService {

    FinancialGoal createGoal(FinancialGoal goal);

    List<FinancialGoal> getAllGoals(Long userId, boolean isAdmin);

    FinancialGoal getGoalById(Long goalId, Long userId, boolean isAdmin);

    FinancialGoal updateGoal(Long goalId, FinancialGoal goal, Long userId, boolean isAdmin);

    void deleteGoal(Long goalId, Long userId, boolean isAdmin);
}