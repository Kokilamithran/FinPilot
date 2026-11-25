package com.finpilot.service.impl;

import com.finpilot.entity.Account;
import com.finpilot.entity.Transaction;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.AccountRepository;
import com.finpilot.repository.TransactionRepository;
import com.finpilot.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        Transaction saved = transactionRepository.save(transaction);
        applyAccountBalanceEffect(saved);
        return saved;
    }

    @Override
    public List<Transaction> getAllTransactions(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return transactionRepository.findAll();
        }
        return transactionRepository.findByUser_UserId(userId);
    }

    @Override
    public Transaction getTransactionById(Long transactionId, Long userId, boolean isAdmin) {
        if (isAdmin) {
            return transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        }
        return transactionRepository.findByTransactionIdAndUser_UserId(transactionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    @Override
    @Transactional
    public Transaction updateTransaction(Long transactionId, Transaction transaction, Long userId, boolean isAdmin) {

        Transaction existing = getTransactionById(transactionId, userId, isAdmin);

        // Reverse old transaction balance effect
        reverseAccountBalanceEffect(existing);

        if (transaction.getTransactionType() != null) {
            existing.setTransactionType(transaction.getTransactionType());
        }
        if (transaction.getAmount() != null) {
            existing.setAmount(transaction.getAmount());
        }
        if (transaction.getBaseCurrencyAmount() != null) {
            existing.setBaseCurrencyAmount(transaction.getBaseCurrencyAmount());
        }
        if (transaction.getCurrency() != null) {
            existing.setCurrency(transaction.getCurrency());
        }
        if (transaction.getExchangeRate() != null) {
            existing.setExchangeRate(transaction.getExchangeRate());
        }
        if (transaction.getTransactionDate() != null) {
            existing.setTransactionDate(transaction.getTransactionDate());
        }
        existing.setMerchant(transaction.getMerchant());
        existing.setNotes(transaction.getNotes());
        existing.setTags(transaction.getTags());

        if (transaction.getIsRecurring() != null) {
            existing.setIsRecurring(transaction.getIsRecurring());
        }
        if (transaction.getReceiptUrl() != null) {
            existing.setReceiptUrl(transaction.getReceiptUrl());
        }
        if (transaction.getIsAutoImported() != null) {
            existing.setIsAutoImported(transaction.getIsAutoImported());
        }
        if (transaction.getImportedAt() != null) {
            existing.setImportedAt(transaction.getImportedAt());
        }
        if (transaction.getAccount() != null) {
            existing.setAccount(transaction.getAccount());
        }
        if (transaction.getCategory() != null) {
            existing.setCategory(transaction.getCategory());
        }
        if (transaction.getRecurringTransaction() != null) {
            existing.setRecurringTransaction(transaction.getRecurringTransaction());
        }

        Transaction updated = transactionRepository.save(existing);
        applyAccountBalanceEffect(updated);
        return updated;
    }

    @Override
    @Transactional
    public void deleteTransaction(Long transactionId, Long userId, boolean isAdmin) {
        Transaction existing = getTransactionById(transactionId, userId, isAdmin);
        reverseAccountBalanceEffect(existing);
        transactionRepository.delete(existing);
    }

    private void applyAccountBalanceEffect(Transaction tx) {
        Account account = tx.getAccount();
        if (account == null || tx.getAmount() == null) return;

        BigDecimal balance = account.getCurrentBalance() != null ? account.getCurrentBalance() : BigDecimal.ZERO;
        String type = tx.getTransactionType();
        if ("INCOME".equalsIgnoreCase(type)) {
            account.setCurrentBalance(balance.add(tx.getAmount()));
        } else if ("EXPENSE".equalsIgnoreCase(type) || "TRANSFER".equalsIgnoreCase(type)) {
            account.setCurrentBalance(balance.subtract(tx.getAmount()));
        }
        accountRepository.save(account);
    }

    private void reverseAccountBalanceEffect(Transaction tx) {
        Account account = tx.getAccount();
        if (account == null || tx.getAmount() == null) return;

        BigDecimal balance = account.getCurrentBalance() != null ? account.getCurrentBalance() : BigDecimal.ZERO;
        String type = tx.getTransactionType();
        if ("INCOME".equalsIgnoreCase(type)) {
            account.setCurrentBalance(balance.subtract(tx.getAmount()));
        } else if ("EXPENSE".equalsIgnoreCase(type) || "TRANSFER".equalsIgnoreCase(type)) {
            account.setCurrentBalance(balance.add(tx.getAmount()));
        }
        accountRepository.save(account);
    }
}