package com.finpilot.service.impl;

import com.finpilot.entity.Account;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.AccountRepository;
import com.finpilot.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return accountRepository.findAll();
        }
        return accountRepository.findByUser_UserId(userId);
    }

    @Override
    public Account getAccountById(Long accountId, Long userId, boolean isAdmin) {
        if (isAdmin) {
            return accountRepository.findById(accountId)
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        }
        return accountRepository.findByAccountIdAndUser_UserId(accountId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Override
    public Account updateAccount(Long accountId, Account account, Long userId, boolean isAdmin) {

        Account existingAccount = getAccountById(accountId, userId, isAdmin);

        existingAccount.setAccountName(account.getAccountName());
        existingAccount.setAccountType(account.getAccountType());
        existingAccount.setCurrency(account.getCurrency());
        existingAccount.setCurrentBalance(account.getCurrentBalance());
        existingAccount.setCreditLimit(account.getCreditLimit());
        existingAccount.setPlaidAccountId(account.getPlaidAccountId());
        existingAccount.setIcon(account.getIcon());
        existingAccount.setColour(account.getColour());
        existingAccount.setIsActive(account.getIsActive());

        return accountRepository.save(existingAccount);
    }

    @Override
    public void deleteAccount(Long accountId, Long userId, boolean isAdmin) {
        Account existingAccount = getAccountById(accountId, userId, isAdmin);
        accountRepository.delete(existingAccount);
    }
}