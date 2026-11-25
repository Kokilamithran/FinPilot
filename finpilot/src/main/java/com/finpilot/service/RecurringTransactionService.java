package com.finpilot.service;

import com.finpilot.entity.RecurringTransaction;

import java.util.List;

public interface RecurringTransactionService {

    RecurringTransaction createRecurringTransaction(RecurringTransaction recurring);

    List<RecurringTransaction> getAllRecurringTransactions(Long userId, boolean isAdmin);

    RecurringTransaction getRecurringTransactionById(Long id, Long userId, boolean isAdmin);

    RecurringTransaction updateRecurringTransaction(Long id, RecurringTransaction recurring, Long userId, boolean isAdmin);

    void deleteRecurringTransaction(Long id, Long userId, boolean isAdmin);
}