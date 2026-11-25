package com.finpilot.controller;

import com.finpilot.dto.request.TransactionRequest;
import com.finpilot.dto.response.TransactionResponse;
import com.finpilot.entity.*;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.mapper.TransactionMapper;
import com.finpilot.repository.AccountRepository;
import com.finpilot.repository.CategoryRepository;
import com.finpilot.repository.RecurringTransactionRepository;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final RecurringTransactionRepository recurringTransactionRepository;
    private final SecurityUtils securityUtils;

    public TransactionController(TransactionService transactionService,
                                 AccountRepository accountRepository,
                                 CategoryRepository categoryRepository,
                                 RecurringTransactionRepository recurringTransactionRepository,
                                 SecurityUtils securityUtils) {
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.recurringTransactionRepository = recurringTransactionRepository;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public TransactionResponse createTransaction(@Valid @RequestBody TransactionRequest request) {

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

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        RecurringTransaction recurringTransaction = null;
        if (request.getRecurringTransactionId() != null) {
            recurringTransaction = recurringTransactionRepository
                    .findById(request.getRecurringTransactionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recurring Transaction not found"));
        }

        Transaction transaction = TransactionMapper.toEntity(
                request,
                currentUser,
                account,
                category,
                recurringTransaction
        );

        return TransactionMapper.toResponse(
                transactionService.createTransaction(transaction)
        );
    }

    @GetMapping
    public List<TransactionResponse> getAllTransactions() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return transactionService.getAllTransactions(currentUser.getUserId(), isAdmin)
                .stream()
                .map(TransactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransactionById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return TransactionMapper.toResponse(
                transactionService.getTransactionById(id, currentUser.getUserId(), isAdmin)
        );
    }

    @PutMapping("/{id}")
    public TransactionResponse updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {

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

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        RecurringTransaction recurringTransaction = null;
        if (request.getRecurringTransactionId() != null) {
            recurringTransaction = recurringTransactionRepository
                    .findById(request.getRecurringTransactionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recurring Transaction not found"));
        }

        Transaction transaction = TransactionMapper.toEntity(
                request,
                currentUser,
                account,
                category,
                recurringTransaction
        );

        return TransactionMapper.toResponse(
                transactionService.updateTransaction(id, transaction, currentUser.getUserId(), isAdmin)
        );
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        transactionService.deleteTransaction(id, currentUser.getUserId(), isAdmin);
        return "Transaction deleted successfully";
    }
}