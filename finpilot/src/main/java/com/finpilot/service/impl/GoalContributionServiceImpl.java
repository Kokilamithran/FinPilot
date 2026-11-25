package com.finpilot.service.impl;

import com.finpilot.entity.FinancialGoal;
import com.finpilot.entity.GoalContribution;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.FinancialGoalRepository;
import com.finpilot.repository.GoalContributionRepository;
import com.finpilot.service.GoalContributionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GoalContributionServiceImpl implements GoalContributionService {

    private final GoalContributionRepository repository;
    private final FinancialGoalRepository goalRepository;

    public GoalContributionServiceImpl(GoalContributionRepository repository, FinancialGoalRepository goalRepository) {
        this.repository = repository;
        this.goalRepository = goalRepository;
    }

    @Override
    @Transactional
    public GoalContribution createContribution(GoalContribution contribution) {
        GoalContribution saved = repository.save(contribution);
        if (saved.getFinancialGoal() != null) {
            updateParentGoalCurrentAmount(saved.getFinancialGoal().getGoalId());
        }
        return saved;
    }

    @Override
    public List<GoalContribution> getAllContributions(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findAll();
        }
        return repository.findByFinancialGoal_User_UserId(userId);
    }

    @Override
    public GoalContribution getContributionById(Long id, Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Goal Contribution not found"));
        }
        return repository.findByContributionIdAndFinancialGoal_User_UserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal Contribution not found"));
    }

    @Override
    @Transactional
    public GoalContribution updateContribution(Long id, GoalContribution contribution, Long userId, boolean isAdmin) {

        GoalContribution existing = getContributionById(id, userId, isAdmin);
        Long oldGoalId = existing.getFinancialGoal() != null ? existing.getFinancialGoal().getGoalId() : null;

        existing.setAmount(contribution.getAmount());
        existing.setContributionDate(contribution.getContributionDate());
        existing.setNotes(contribution.getNotes());
        existing.setFinancialGoal(contribution.getFinancialGoal());

        GoalContribution saved = repository.save(existing);

        if (oldGoalId != null) {
            updateParentGoalCurrentAmount(oldGoalId);
        }
        if (saved.getFinancialGoal() != null && (oldGoalId == null || !oldGoalId.equals(saved.getFinancialGoal().getGoalId()))) {
            updateParentGoalCurrentAmount(saved.getFinancialGoal().getGoalId());
        }

        return saved;
    }

    @Override
    @Transactional
    public void deleteContribution(Long id, Long userId, boolean isAdmin) {
        GoalContribution existing = getContributionById(id, userId, isAdmin);
        Long goalId = existing.getFinancialGoal() != null ? existing.getFinancialGoal().getGoalId() : null;
        repository.delete(existing);
        if (goalId != null) {
            updateParentGoalCurrentAmount(goalId);
        }
    }

    private void updateParentGoalCurrentAmount(Long goalId) {
        if (goalId == null) return;
        FinancialGoal goal = goalRepository.findById(goalId).orElse(null);
        if (goal != null) {
            BigDecimal total = repository.sumAmountByFinancialGoal_GoalId(goalId);
            goal.setCurrentAmount(total != null ? total : BigDecimal.ZERO);
            if (goal.getTargetAmount() != null && goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
                goal.setStatus("ACHIEVED");
            }
            goalRepository.save(goal);
        }
    }
}