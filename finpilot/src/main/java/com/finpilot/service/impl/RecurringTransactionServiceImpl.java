package com.finpilot.service.impl;

import com.finpilot.entity.RecurringTransaction;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.RecurringTransactionRepository;
import com.finpilot.service.RecurringTransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecurringTransactionServiceImpl implements RecurringTransactionService {

    private final RecurringTransactionRepository repository;

    public RecurringTransactionServiceImpl(RecurringTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public RecurringTransaction createRecurringTransaction(RecurringTransaction recurring) {
        return repository.save(recurring);
    }

    @Override
    public List<RecurringTransaction> getAllRecurringTransactions(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findAll();
        }
        return repository.findByUser_UserId(userId);
    }

    @Override
    public RecurringTransaction getRecurringTransactionById(Long id, Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Recurring Transaction not found"));
        }
        return repository.findByRecurringIdAndUser_UserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recurring Transaction not found"));
    }

    @Override
    public RecurringTransaction updateRecurringTransaction(Long id, RecurringTransaction recurring, Long userId, boolean isAdmin) {

        RecurringTransaction existing = getRecurringTransactionById(id, userId, isAdmin);

        existing.setName(recurring.getName());
        existing.setTransactionType(recurring.getTransactionType());
        existing.setAmount(recurring.getAmount());
        existing.setMerchant(recurring.getMerchant());
        existing.setFrequency(recurring.getFrequency());
        existing.setNextDueDate(recurring.getNextDueDate());
        existing.setStartDate(recurring.getStartDate());
        existing.setEndDate(recurring.getEndDate());
        existing.setIsActive(recurring.getIsActive());
        existing.setAutoLog(recurring.getAutoLog());
        existing.setAccount(recurring.getAccount());
        existing.setCategory(recurring.getCategory());

        return repository.save(existing);
    }

    @Override
    public void deleteRecurringTransaction(Long id, Long userId, boolean isAdmin) {
        RecurringTransaction existing = getRecurringTransactionById(id, userId, isAdmin);
        repository.delete(existing);
    }
}