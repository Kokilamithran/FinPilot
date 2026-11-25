package com.finpilot.service;

import com.finpilot.entity.GroupExpenseShare;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.GroupExpenseShareRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupExpenseShareService {

    private final GroupExpenseShareRepository repository;

    public GroupExpenseShareService(GroupExpenseShareRepository repository) {
        this.repository = repository;
    }

    public List<GroupExpenseShare> getAll(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findAll();
        }
        return repository.findByUser_UserId(userId);
    }

    public GroupExpenseShare getById(Long id, Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Group Expense Share not found"));
        }
        return repository.findByShareIdAndUser_UserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Group Expense Share not found"));
    }

    public GroupExpenseShare create(GroupExpenseShare share) {
        return repository.save(share);
    }

    public GroupExpenseShare update(Long id, GroupExpenseShare updatedShare, Long userId, boolean isAdmin) {

        GroupExpenseShare existing = getById(id, userId, isAdmin);

        existing.setGroupExpense(updatedShare.getGroupExpense());
        existing.setShareAmount(updatedShare.getShareAmount());
        existing.setIsSettled(updatedShare.getIsSettled());
        existing.setSettledAt(updatedShare.getSettledAt());
        existing.setSettledMode(updatedShare.getSettledMode());

        return repository.save(existing);
    }

    public void delete(Long id, Long userId, boolean isAdmin) {
        GroupExpenseShare existing = getById(id, userId, isAdmin);
        repository.delete(existing);
    }
}