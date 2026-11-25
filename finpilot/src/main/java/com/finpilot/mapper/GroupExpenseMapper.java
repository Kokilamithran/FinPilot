package com.finpilot.mapper;

import com.finpilot.dto.request.GroupExpenseRequest;
import com.finpilot.dto.response.GroupExpenseResponse;
import com.finpilot.entity.Group;
import com.finpilot.entity.GroupExpense;
import com.finpilot.entity.Transaction;
import com.finpilot.entity.User;

public class GroupExpenseMapper {

    public static GroupExpense toEntity(GroupExpenseRequest request, Group group, Transaction transaction, User createdBy) {
        GroupExpense expense = new GroupExpense();
        expense.setGroup(group);
        expense.setTransaction(transaction);
        expense.setTotalAmount(request.getTotalAmount());
        expense.setSplitType(request.getSplitType());
        expense.setCreatedBy(createdBy);
        return expense;
    }

    public static GroupExpenseResponse toResponse(GroupExpense expense) {
        GroupExpenseResponse response = new GroupExpenseResponse();
        response.setGroupExpenseId(expense.getGroupExpenseId());
        response.setGroupId(expense.getGroup() != null ? expense.getGroup().getGroupId() : null);
        response.setTransactionId(expense.getTransaction() != null ? expense.getTransaction().getTransactionId() : null);
        response.setTotalAmount(expense.getTotalAmount());
        response.setSplitType(expense.getSplitType());
        response.setCreatedBy(expense.getCreatedBy() != null ? expense.getCreatedBy().getUserId() : null);
        response.setCreatedDate(expense.getCreatedDate());
        return response;
    }
}
