package com.finpilot.mapper;

import com.finpilot.dto.request.BudgetRequest;
import com.finpilot.dto.response.BudgetResponse;
import com.finpilot.entity.Budget;
import com.finpilot.entity.User;

public class BudgetMapper {

    public static Budget toEntity(BudgetRequest request, User user) {

        Budget budget = new Budget();

        budget.setBudgetName(request.getBudgetName());
        budget.setPeriodType(request.getPeriodType());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());
        budget.setTotalAmount(request.getTotalAmount());
        budget.setCurrency(request.getCurrency());
        budget.setIsActive(request.getIsActive());
        budget.setUser(user);

        return budget;
    }

    public static BudgetResponse toResponse(Budget budget) {

        BudgetResponse response = new BudgetResponse();

        response.setBudgetId(budget.getBudgetId());
        response.setBudgetName(budget.getBudgetName());
        response.setPeriodType(budget.getPeriodType());
        response.setStartDate(budget.getStartDate());
        response.setEndDate(budget.getEndDate());
        response.setTotalAmount(budget.getTotalAmount());
        response.setCurrency(budget.getCurrency());
        response.setIsActive(budget.getIsActive());
        response.setUserId(budget.getUser().getUserId());
        response.setCreatedDate(budget.getCreatedDate());
        response.setModifiedDate(budget.getModifiedDate());

        return response;
    }
}