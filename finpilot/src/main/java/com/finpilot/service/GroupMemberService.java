package com.finpilot.service;

import com.finpilot.entity.GroupMember;

import java.util.List;

public interface GroupMemberService {

    GroupMember createGroupMember(GroupMember member);

    List<GroupMember> getAllGroupMembers(Long userId, boolean isAdmin);

    GroupMember getGroupMemberById(Long id, Long userId, boolean isAdmin);

    GroupMember updateGroupMember(Long id, GroupMember member, Long userId, boolean isAdmin);

    void deleteGroupMember(Long id, Long userId, boolean isAdmin);
}