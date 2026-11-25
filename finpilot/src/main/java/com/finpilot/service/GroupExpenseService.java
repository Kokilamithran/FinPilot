package com.finpilot.service;

import com.finpilot.entity.GroupExpense;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.GroupExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupExpenseService {

    private final GroupExpenseRepository repository;

    public GroupExpenseService(GroupExpenseRepository repository) {
        this.repository = repository;
    }

    public List<GroupExpense> getAll(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findAll();
        }
        return repository.findByCreatedBy_UserId(userId);
    }

    public GroupExpense getById(Long id, Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Group Expense not found"));
        }
        return repository.findByGroupExpenseIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Group Expense not found"));
    }

    public GroupExpense create(GroupExpense groupExpense) {
        return repository.save(groupExpense);
    }

    public GroupExpense update(Long id, GroupExpense updatedGroupExpense, Long userId, boolean isAdmin) {

        GroupExpense existing = getById(id, userId, isAdmin);

        existing.setGroup(updatedGroupExpense.getGroup());
        existing.setTransaction(updatedGroupExpense.getTransaction());
        existing.setTotalAmount(updatedGroupExpense.getTotalAmount());
        existing.setSplitType(updatedGroupExpense.getSplitType());

        return repository.save(existing);
    }

    public void delete(Long id, Long userId, boolean isAdmin) {
        GroupExpense existing = getById(id, userId, isAdmin);
        repository.delete(existing);
    }
}