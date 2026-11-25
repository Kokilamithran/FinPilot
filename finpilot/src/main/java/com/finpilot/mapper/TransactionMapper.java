package com.finpilot.mapper;

import com.finpilot.dto.request.TransactionRequest;
import com.finpilot.dto.response.TransactionResponse;
import com.finpilot.entity.Account;
import com.finpilot.entity.Category;
import com.finpilot.entity.RecurringTransaction;
import com.finpilot.entity.Transaction;
import com.finpilot.entity.User;

public class TransactionMapper {

    public static Transaction toEntity(TransactionRequest request,
                                       User user,
                                       Account account,
                                       Category category,
                                       RecurringTransaction recurringTransaction) {

        Transaction transaction = new Transaction();

        transaction.setTransactionType(request.getTransactionType());
        transaction.setAmount(request.getAmount());
        transaction.setBaseCurrencyAmount(request.getBaseCurrencyAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setExchangeRate(request.getExchangeRate());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setMerchant(request.getMerchant());
        transaction.setNotes(request.getNotes());
        transaction.setTags(request.getTags());
        transaction.setIsRecurring(request.getIsRecurring());
        transaction.setReceiptUrl(request.getReceiptUrl());
        transaction.setIsAutoImported(request.getIsAutoImported());
        transaction.setImportedAt(request.getImportedAt());

        transaction.setUser(user);
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setRecurringTransaction(recurringTransaction);

        return transaction;
    }

    public static TransactionResponse toResponse(Transaction transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setTransactionId(transaction.getTransactionId());
        response.setTransactionType(transaction.getTransactionType());
        response.setAmount(transaction.getAmount());
        response.setBaseCurrencyAmount(transaction.getBaseCurrencyAmount());
        response.setCurrency(transaction.getCurrency());
        response.setExchangeRate(transaction.getExchangeRate());
        response.setTransactionDate(transaction.getTransactionDate());
        response.setMerchant(transaction.getMerchant());
        response.setNotes(transaction.getNotes());
        response.setTags(transaction.getTags());
        response.setIsRecurring(transaction.getIsRecurring());
        response.setReceiptUrl(transaction.getReceiptUrl());
        response.setIsAutoImported(transaction.getIsAutoImported());
        response.setImportedAt(transaction.getImportedAt());

        response.setUserId(transaction.getUser().getUserId());
        response.setAccountId(transaction.getAccount().getAccountId());

        response.setCategoryId(
                transaction.getCategory() != null
                        ? transaction.getCategory().getCategoryId()
                        : null
        );

        response.setRecurringTransactionId(
                transaction.getRecurringTransaction() != null
                        ? transaction.getRecurringTransaction().getRecurringId()
                        : null
        );

        response.setCreatedDate(transaction.getCreatedDate());
        response.setModifiedDate(transaction.getModifiedDate());

        return response;
    }
}