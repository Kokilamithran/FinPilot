package com.finpilot.mapper;

import com.finpilot.dto.request.RecurringTransactionRequest;
import com.finpilot.dto.response.RecurringTransactionResponse;
import com.finpilot.entity.Account;
import com.finpilot.entity.Category;
import com.finpilot.entity.RecurringTransaction;
import com.finpilot.entity.User;

public class RecurringTransactionMapper {

    public static RecurringTransaction toEntity(RecurringTransactionRequest request,
                                                User user,
                                                Account account,
                                                Category category) {

        RecurringTransaction recurring = new RecurringTransaction();

        recurring.setName(request.getName());
        recurring.setTransactionType(request.getTransactionType());
        recurring.setAmount(request.getAmount());
        recurring.setMerchant(request.getMerchant());
        recurring.setFrequency(request.getFrequency());
        recurring.setNextDueDate(request.getNextDueDate());
        recurring.setStartDate(request.getStartDate());
        recurring.setEndDate(request.getEndDate());
        recurring.setIsActive(request.getIsActive());
        recurring.setAutoLog(request.getAutoLog());

        recurring.setUser(user);
        recurring.setAccount(account);
        recurring.setCategory(category);

        return recurring;
    }

    public static RecurringTransactionResponse toResponse(RecurringTransaction recurring) {

        RecurringTransactionResponse response = new RecurringTransactionResponse();

        response.setRecurringId(recurring.getRecurringId());
        response.setName(recurring.getName());
        response.setTransactionType(recurring.getTransactionType());
        response.setAmount(recurring.getAmount());
        response.setMerchant(recurring.getMerchant());
        response.setFrequency(recurring.getFrequency());
        response.setNextDueDate(recurring.getNextDueDate());
        response.setStartDate(recurring.getStartDate());
        response.setEndDate(recurring.getEndDate());
        response.setIsActive(recurring.getIsActive());
        response.setAutoLog(recurring.getAutoLog());

        response.setUserId(recurring.getUser().getUserId());
        response.setAccountId(recurring.getAccount().getAccountId());
        response.setCategoryId(recurring.getCategory().getCategoryId());

        response.setCreatedDate(recurring.getCreatedDate());
        response.setModifiedDate(recurring.getModifiedDate());

        return response;
    }
}