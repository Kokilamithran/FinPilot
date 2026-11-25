package com.finpilot.service.impl;

import com.finpilot.entity.FinancialGoal;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.FinancialGoalRepository;
import com.finpilot.repository.GoalContributionRepository;
import com.finpilot.service.FinancialGoalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FinancialGoalServiceImpl implements FinancialGoalService {

    private final FinancialGoalRepository financialGoalRepository;
    private final GoalContributionRepository goalContributionRepository;

    public FinancialGoalServiceImpl(FinancialGoalRepository financialGoalRepository, GoalContributionRepository goalContributionRepository) {
        this.financialGoalRepository = financialGoalRepository;
        this.goalContributionRepository = goalContributionRepository;
    }

    @Override
    public FinancialGoal createGoal(FinancialGoal goal) {
        return financialGoalRepository.save(goal);
    }

    @Override
    public List<FinancialGoal> getAllGoals(Long userId, boolean isAdmin) {
        List<FinancialGoal> goals;
        if (isAdmin) {
            goals = financialGoalRepository.findAll();
        } else {
            goals = financialGoalRepository.findByUser_UserId(userId);
        }
        goals.forEach(this::syncCurrentAmount);
        return goals;
    }

    @Override
    public FinancialGoal getGoalById(Long goalId, Long userId, boolean isAdmin) {
        FinancialGoal goal;
        if (isAdmin) {
            goal = financialGoalRepository.findById(goalId)
                    .orElseThrow(() -> new ResourceNotFoundException("Financial Goal not found"));
        } else {
            goal = financialGoalRepository.findByGoalIdAndUser_UserId(goalId, userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Financial Goal not found"));
        }
        syncCurrentAmount(goal);
        return goal;
    }

    @Override
    public FinancialGoal updateGoal(Long goalId, FinancialGoal goal, Long userId, boolean isAdmin) {

        FinancialGoal existing = getGoalById(goalId, userId, isAdmin);

        existing.setGoalName(goal.getGoalName());
        existing.setGoalCategory(goal.getGoalCategory());
        existing.setTargetAmount(goal.getTargetAmount());
        if (goal.getCurrentAmount() != null) {
            existing.setCurrentAmount(goal.getCurrentAmount());
        }
        existing.setTargetDate(goal.getTargetDate());
        existing.setPriority(goal.getPriority());
        existing.setStatus(goal.getStatus());
        existing.setLinkedAccount(goal.getLinkedAccount());
        existing.setAbandonedReason(goal.getAbandonedReason());

        syncCurrentAmount(existing);
        return financialGoalRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteGoal(Long goalId, Long userId, boolean isAdmin) {
        FinancialGoal existing = getGoalById(goalId, userId, isAdmin);
        goalContributionRepository.deleteByFinancialGoal_GoalId(goalId);
        financialGoalRepository.delete(existing);
    }

    private void syncCurrentAmount(FinancialGoal goal) {
        if (goal != null && goal.getGoalId() != null) {
            BigDecimal total = goalContributionRepository.sumAmountByFinancialGoal_GoalId(goal.getGoalId());
            if (total != null && total.compareTo(BigDecimal.ZERO) > 0) {
                goal.setCurrentAmount(total);
                if (goal.getTargetAmount() != null && goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
                    goal.setStatus("ACHIEVED");
                }
            }
        }
    }
}