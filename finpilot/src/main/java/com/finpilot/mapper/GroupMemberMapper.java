package com.finpilot.mapper;

import com.finpilot.dto.request.GroupMemberRequest;
import com.finpilot.dto.response.GroupMemberResponse;
import com.finpilot.entity.Group;
import com.finpilot.entity.GroupMember;
import com.finpilot.entity.User;

public class GroupMemberMapper {

    public static GroupMember toEntity(GroupMemberRequest request,
                                       Group group,
                                       User user) {

        GroupMember member = new GroupMember();

        member.setGroup(group);
        member.setUser(user);
        member.setMemberRole(request.getMemberRole());

        return member;
    }

    public static GroupMemberResponse toResponse(GroupMember member) {

        GroupMemberResponse response = new GroupMemberResponse();

        response.setGroupMemberId(member.getGroupMemberId());
        response.setGroupId(member.getGroup().getGroupId());
        response.setUserId(member.getUser().getUserId());
        response.setMemberRole(member.getMemberRole());
        response.setJoinedDate(member.getJoinedDate());

        return response;
    }
}