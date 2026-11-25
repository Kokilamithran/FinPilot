package com.finpilot.controller;

import com.finpilot.dto.request.GroupExpenseShareRequest;
import com.finpilot.dto.response.GroupExpenseShareResponse;
import com.finpilot.entity.GroupExpense;
import com.finpilot.entity.GroupExpenseShare;
import com.finpilot.entity.User;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.mapper.GroupExpenseShareMapper;
import com.finpilot.repository.GroupExpenseRepository;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.GroupExpenseShareService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/group-expense-shares")
public class GroupExpenseShareController {

    private final GroupExpenseShareService service;
    private final GroupExpenseRepository groupExpenseRepository;
    private final SecurityUtils securityUtils;

    public GroupExpenseShareController(GroupExpenseShareService service,
                                      GroupExpenseRepository groupExpenseRepository,
                                      SecurityUtils securityUtils) {
        this.service = service;
        this.groupExpenseRepository = groupExpenseRepository;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public List<GroupExpenseShareResponse> getAll() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();
        return service.getAll(currentUser.getUserId(), isAdmin)
                .stream()
                .map(GroupExpenseShareMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GroupExpenseShareResponse getById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();
        return GroupExpenseShareMapper.toResponse(service.getById(id, currentUser.getUserId(), isAdmin));
    }

    @PostMapping
    public GroupExpenseShareResponse create(@Valid @RequestBody GroupExpenseShareRequest request) {
        User currentUser = securityUtils.getCurrentUser();

        GroupExpense groupExpense = groupExpenseRepository.findById(request.getGroupExpenseId())
                .orElseThrow(() -> new ResourceNotFoundException("Group Expense not found"));

        GroupExpenseShare share = GroupExpenseShareMapper.toEntity(request, groupExpense, currentUser);
        return GroupExpenseShareMapper.toResponse(service.create(share));
    }

    @PutMapping("/{id}")
    public GroupExpenseShareResponse update(@PathVariable Long id,
                                            @Valid @RequestBody GroupExpenseShareRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        GroupExpense groupExpense = groupExpenseRepository.findById(request.getGroupExpenseId())
                .orElseThrow(() -> new ResourceNotFoundException("Group Expense not found"));

        GroupExpenseShare share = GroupExpenseShareMapper.toEntity(request, groupExpense, currentUser);
        return GroupExpenseShareMapper.toResponse(service.update(id, share, currentUser.getUserId(), isAdmin));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();
        service.delete(id, currentUser.getUserId(), isAdmin);
        return "Group Expense Share deleted successfully";
    }
}