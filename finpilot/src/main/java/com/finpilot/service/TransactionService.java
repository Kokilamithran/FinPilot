package com.finpilot.service;

import com.finpilot.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);

    List<Transaction> getAllTransactions(Long userId, boolean isAdmin);

    Transaction getTransactionById(Long transactionId, Long userId, boolean isAdmin);

    Transaction updateTransaction(Long transactionId, Transaction transaction, Long userId, boolean isAdmin);

    void deleteTransaction(Long transactionId, Long userId, boolean isAdmin);
}