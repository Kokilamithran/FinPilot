package com.finpilot.mapper;

import com.finpilot.dto.request.FinancialGoalRequest;
import com.finpilot.dto.response.FinancialGoalResponse;
import com.finpilot.entity.Account;
import com.finpilot.entity.FinancialGoal;
import com.finpilot.entity.User;

public class FinancialGoalMapper {

    public static FinancialGoal toEntity(FinancialGoalRequest request,
                                         User user,
                                         Account account) {

        FinancialGoal goal = new FinancialGoal();

        goal.setGoalName(request.getGoalName());
        goal.setGoalCategory(request.getGoalCategory());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setCurrentAmount(request.getCurrentAmount());
        goal.setTargetDate(request.getTargetDate());
        goal.setPriority(request.getPriority());
        goal.setStatus(request.getStatus());
        goal.setUser(user);
        goal.setLinkedAccount(account);
        goal.setAbandonedReason(request.getAbandonedReason());

        return goal;
    }

    public static FinancialGoalResponse toResponse(FinancialGoal goal) {

        FinancialGoalResponse response = new FinancialGoalResponse();

        response.setGoalId(goal.getGoalId());
        response.setGoalName(goal.getGoalName());
        response.setGoalCategory(goal.getGoalCategory());
        response.setTargetAmount(goal.getTargetAmount());
        response.setCurrentAmount(goal.getCurrentAmount());
        response.setTargetDate(goal.getTargetDate());
        response.setPriority(goal.getPriority());
        response.setStatus(goal.getStatus());

        response.setUserId(goal.getUser().getUserId());

        if (goal.getLinkedAccount() != null) {
            response.setLinkedAccountId(goal.getLinkedAccount().getAccountId());
        }

        response.setCreatedDate(goal.getCreatedDate());
        response.setModifiedDate(goal.getModifiedDate());
        response.setCompletedAt(goal.getCompletedAt());
        response.setAbandonedReason(goal.getAbandonedReason());

        return response;
    }
}