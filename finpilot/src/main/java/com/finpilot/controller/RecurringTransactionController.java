package com.finpilot.controller;

import com.finpilot.dto.request.RecurringTransactionRequest;
import com.finpilot.dto.response.RecurringTransactionResponse;
import com.finpilot.entity.Account;
import com.finpilot.entity.Category;
import com.finpilot.entity.RecurringTransaction;
import com.finpilot.entity.User;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.mapper.RecurringTransactionMapper;
import com.finpilot.repository.AccountRepository;
import com.finpilot.repository.CategoryRepository;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.RecurringTransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recurring-transactions")
public class RecurringTransactionController {

    private final RecurringTransactionService service;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final SecurityUtils securityUtils;

    public RecurringTransactionController(
            RecurringTransactionService service,
            AccountRepository accountRepository,
            CategoryRepository categoryRepository,
            SecurityUtils securityUtils) {

        this.service = service;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public RecurringTransactionResponse createRecurringTransaction(
            @Valid @RequestBody RecurringTransactionRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Account account;
        if (isAdmin) {
            account = accountRepository.findById(request.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        } else {
            account = accountRepository.findByAccountIdAndUser_UserId(request.getAccountId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        RecurringTransaction recurring = RecurringTransactionMapper.toEntity(
                request,
                currentUser,
                account,
                category
        );

        return RecurringTransactionMapper.toResponse(
                service.createRecurringTransaction(recurring)
        );
    }

    @GetMapping
    public List<RecurringTransactionResponse> getAllRecurringTransactions() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return service.getAllRecurringTransactions(currentUser.getUserId(), isAdmin)
                .stream()
                .map(RecurringTransactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RecurringTransactionResponse getRecurringTransactionById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return RecurringTransactionMapper.toResponse(
                service.getRecurringTransactionById(id, currentUser.getUserId(), isAdmin)
        );
    }

    @PutMapping("/{id}")
    public RecurringTransactionResponse updateRecurringTransaction(
            @PathVariable Long id,
            @Valid @RequestBody RecurringTransactionRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Account account;
        if (isAdmin) {
            account = accountRepository.findById(request.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        } else {
            account = accountRepository.findByAccountIdAndUser_UserId(request.getAccountId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        RecurringTransaction recurring = RecurringTransactionMapper.toEntity(
                request,
                currentUser,
                account,
                category
        );

        return RecurringTransactionMapper.toResponse(
                service.updateRecurringTransaction(id, recurring, currentUser.getUserId(), isAdmin)
        );
    }

    @DeleteMapping("/{id}")
    public String deleteRecurringTransaction(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        service.deleteRecurringTransaction(id, currentUser.getUserId(), isAdmin);
        return "Recurring Transaction deleted successfully";
    }
}