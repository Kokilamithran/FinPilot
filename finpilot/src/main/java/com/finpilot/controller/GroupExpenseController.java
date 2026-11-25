package com.finpilot.controller;

import com.finpilot.dto.request.GroupExpenseRequest;
import com.finpilot.dto.response.GroupExpenseResponse;
import com.finpilot.entity.Group;
import com.finpilot.entity.GroupExpense;
import com.finpilot.entity.Transaction;
import com.finpilot.entity.User;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.mapper.GroupExpenseMapper;
import com.finpilot.repository.GroupRepository;
import com.finpilot.repository.TransactionRepository;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.GroupExpenseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/group-expenses")
public class GroupExpenseController {

    private final GroupExpenseService service;
    private final GroupRepository groupRepository;
    private final TransactionRepository transactionRepository;
    private final SecurityUtils securityUtils;

    public GroupExpenseController(GroupExpenseService service,
                                  GroupRepository groupRepository,
                                  TransactionRepository transactionRepository,
                                  SecurityUtils securityUtils) {
        this.service = service;
        this.groupRepository = groupRepository;
        this.transactionRepository = transactionRepository;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public List<GroupExpenseResponse> getAll() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();
        return service.getAll(currentUser.getUserId(), isAdmin)
                .stream()
                .map(GroupExpenseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GroupExpenseResponse getById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();
        return GroupExpenseMapper.toResponse(service.getById(id, currentUser.getUserId(), isAdmin));
    }

    @PostMapping
    public GroupExpenseResponse create(@Valid @RequestBody GroupExpenseRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Group group;
        if (isAdmin) {
            group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        } else {
            group = groupRepository.findByGroupIdAndCreatedBy_UserId(request.getGroupId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        }

        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        GroupExpense groupExpense = GroupExpenseMapper.toEntity(request, group, transaction, currentUser);
        return GroupExpenseMapper.toResponse(service.create(groupExpense));
    }

    @PutMapping("/{id}")
    public GroupExpenseResponse update(@PathVariable Long id,
                                       @Valid @RequestBody GroupExpenseRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Group group;
        if (isAdmin) {
            group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        } else {
            group = groupRepository.findByGroupIdAndCreatedBy_UserId(request.getGroupId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        }

        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        GroupExpense groupExpense = GroupExpenseMapper.toEntity(request, group, transaction, currentUser);
        return GroupExpenseMapper.toResponse(service.update(id, groupExpense, currentUser.getUserId(), isAdmin));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();
        service.delete(id, currentUser.getUserId(), isAdmin);
        return "Group Expense deleted successfully";
    }
}