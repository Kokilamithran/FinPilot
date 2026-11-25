package com.finpilot.service;

import com.finpilot.entity.GoalContribution;

import java.util.List;

public interface GoalContributionService {

    GoalContribution createContribution(GoalContribution contribution);

    List<GoalContribution> getAllContributions(Long userId, boolean isAdmin);

    GoalContribution getContributionById(Long id, Long userId, boolean isAdmin);

    GoalContribution updateContribution(Long id, GoalContribution contribution, Long userId, boolean isAdmin);

    void deleteContribution(Long id, Long userId, boolean isAdmin);
}