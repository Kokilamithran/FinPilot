package com.finpilot.controller;

import com.finpilot.dto.request.FinancialGoalRequest;
import com.finpilot.dto.response.FinancialGoalResponse;
import com.finpilot.entity.Account;
import com.finpilot.entity.FinancialGoal;
import com.finpilot.entity.User;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.mapper.FinancialGoalMapper;
import com.finpilot.repository.AccountRepository;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.FinancialGoalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/goals")
public class FinancialGoalController {

    private final FinancialGoalService financialGoalService;
    private final AccountRepository accountRepository;
    private final SecurityUtils securityUtils;

    public FinancialGoalController(FinancialGoalService financialGoalService,
                                   AccountRepository accountRepository,
                                   SecurityUtils securityUtils) {
        this.financialGoalService = financialGoalService;
        this.accountRepository = accountRepository;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public FinancialGoalResponse createGoal(@Valid @RequestBody FinancialGoalRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Account account = null;

        if (request.getLinkedAccountId() != null) {
            if (isAdmin) {
                account = accountRepository.findById(request.getLinkedAccountId())
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            } else {
                account = accountRepository.findByAccountIdAndUser_UserId(request.getLinkedAccountId(), currentUser.getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            }
        }

        FinancialGoal goal = FinancialGoalMapper.toEntity(request, currentUser, account);

        return FinancialGoalMapper.toResponse(
                financialGoalService.createGoal(goal)
        );
    }

    @GetMapping
    public List<FinancialGoalResponse> getAllGoals() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return financialGoalService.getAllGoals(currentUser.getUserId(), isAdmin)
                .stream()
                .map(FinancialGoalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FinancialGoalResponse getGoalById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return FinancialGoalMapper.toResponse(
                financialGoalService.getGoalById(id, currentUser.getUserId(), isAdmin)
        );
    }

    @PutMapping("/{id}")
    public FinancialGoalResponse updateGoal(
            @PathVariable Long id,
            @Valid @RequestBody FinancialGoalRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Account account = null;

        if (request.getLinkedAccountId() != null) {
            if (isAdmin) {
                account = accountRepository.findById(request.getLinkedAccountId())
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            } else {
                account = accountRepository.findByAccountIdAndUser_UserId(request.getLinkedAccountId(), currentUser.getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            }
        }

        FinancialGoal goal = FinancialGoalMapper.toEntity(
                request,
                currentUser,
                account
        );

        return FinancialGoalMapper.toResponse(
                financialGoalService.updateGoal(id, goal, currentUser.getUserId(), isAdmin)
        );
    }

    @DeleteMapping("/{id}")
    public String deleteGoal(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        financialGoalService.deleteGoal(id, currentUser.getUserId(), isAdmin);
        return "Financial Goal deleted successfully";
    }
}