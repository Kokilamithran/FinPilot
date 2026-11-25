package com.finpilot.mapper;

import com.finpilot.dto.request.GroupExpenseShareRequest;
import com.finpilot.dto.response.GroupExpenseShareResponse;
import com.finpilot.entity.GroupExpense;
import com.finpilot.entity.GroupExpenseShare;
import com.finpilot.entity.User;

public class GroupExpenseShareMapper {

    public static GroupExpenseShare toEntity(GroupExpenseShareRequest request, GroupExpense groupExpense, User user) {
        GroupExpenseShare share = new GroupExpenseShare();
        share.setGroupExpense(groupExpense);
        share.setUser(user);
        share.setShareAmount(request.getShareAmount());
        share.setIsSettled(request.getIsSettled());
        share.setSettledAt(request.getSettledAt());
        share.setSettledMode(request.getSettledMode());
        return share;
    }

    public static GroupExpenseShareResponse toResponse(GroupExpenseShare share) {
        GroupExpenseShareResponse response = new GroupExpenseShareResponse();
        response.setShareId(share.getShareId());
        response.setGroupExpenseId(share.getGroupExpense() != null ? share.getGroupExpense().getGroupExpenseId() : null);
        response.setUserId(share.getUser() != null ? share.getUser().getUserId() : null);
        response.setShareAmount(share.getShareAmount());
        response.setIsSettled(share.getIsSettled());
        response.setSettledAt(share.getSettledAt());
        response.setSettledMode(share.getSettledMode());
        return response;
    }
}
