package com.finpilot.mapper;

import com.finpilot.dto.request.AccountRequest;
import com.finpilot.dto.response.AccountResponse;
import com.finpilot.entity.Account;
import com.finpilot.entity.User;

public class AccountMapper {

    public static Account toEntity(AccountRequest request, User user) {

        Account account = new Account();

        account.setAccountName(request.getAccountName());
        account.setAccountType(request.getAccountType());
        account.setCurrency(request.getCurrency());
        account.setCurrentBalance(request.getCurrentBalance());
        account.setCreditLimit(request.getCreditLimit());
        account.setPlaidAccountId(request.getPlaidAccountId());
        account.setIcon(request.getIcon());
        account.setColour(request.getColour());
        account.setIsActive(request.getIsActive());
        account.setUser(user);

        return account;
    }

    public static AccountResponse toResponse(Account account) {

        AccountResponse response = new AccountResponse();

        response.setAccountId(account.getAccountId());
        response.setAccountName(account.getAccountName());
        response.setAccountType(account.getAccountType());
        response.setCurrency(account.getCurrency());
        response.setCurrentBalance(account.getCurrentBalance());
        response.setCreditLimit(account.getCreditLimit());
        response.setPlaidAccountId(account.getPlaidAccountId());
        response.setIcon(account.getIcon());
        response.setColour(account.getColour());
        response.setIsActive(account.getIsActive());
        response.setUserId(account.getUser().getUserId());
        response.setCreatedDate(account.getCreatedDate());
        response.setModifiedDate(account.getModifiedDate());

        return response;
    }
}
