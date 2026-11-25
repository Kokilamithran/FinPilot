package com.finpilot.mapper;

import com.finpilot.dto.request.GoalContributionRequest;
import com.finpilot.dto.response.GoalContributionResponse;
import com.finpilot.entity.FinancialGoal;
import com.finpilot.entity.GoalContribution;

public class GoalContributionMapper {

    public static GoalContribution toEntity(GoalContributionRequest request,
                                            FinancialGoal goal) {

        GoalContribution contribution = new GoalContribution();

        contribution.setAmount(request.getAmount());
        contribution.setContributionDate(request.getContributionDate());
        contribution.setNotes(request.getNotes());
        contribution.setFinancialGoal(goal);

        return contribution;
    }

    public static GoalContributionResponse toResponse(GoalContribution contribution) {

        GoalContributionResponse response = new GoalContributionResponse();

        response.setContributionId(contribution.getContributionId());
        response.setAmount(contribution.getAmount());
        response.setContributionDate(contribution.getContributionDate());
        response.setNotes(contribution.getNotes());
        response.setGoalId(contribution.getFinancialGoal().getGoalId());
        response.setCreatedDate(contribution.getCreatedDate());

        return response;
    }
}