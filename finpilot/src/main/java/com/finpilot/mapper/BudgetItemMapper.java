package com.finpilot.mapper;

import com.finpilot.dto.request.BudgetItemRequest;
import com.finpilot.dto.response.BudgetItemResponse;
import com.finpilot.entity.Budget;
import com.finpilot.entity.BudgetItem;
import com.finpilot.entity.Category;

public class BudgetItemMapper {

    public static BudgetItem toEntity(BudgetItemRequest request,
                                      Budget budget,
                                      Category category) {

        BudgetItem item = new BudgetItem();

        item.setBudget(budget);
        item.setCategory(category);
        item.setAllocatedAmount(request.getAllocatedAmount());
        item.setSpentAmount(request.getSpentAmount());
        item.setAlertPercentage(request.getAlertPercentage());
        item.setRolloverEnabled(request.getRolloverEnabled());
        item.setRolloverAmount(request.getRolloverAmount());
        item.setNotes(request.getNotes());

        return item;
    }

    public static BudgetItemResponse toResponse(BudgetItem item) {

        BudgetItemResponse response = new BudgetItemResponse();

        response.setItemId(item.getItemId());
        response.setBudgetId(item.getBudget().getBudgetId());
        response.setCategoryId(item.getCategory().getCategoryId());
        response.setAllocatedAmount(item.getAllocatedAmount());
        response.setSpentAmount(item.getSpentAmount());
        response.setAlertPercentage(item.getAlertPercentage());
        response.setRolloverEnabled(item.getRolloverEnabled());
        response.setRolloverAmount(item.getRolloverAmount());
        response.setNotes(item.getNotes());

        return response;
    }
}