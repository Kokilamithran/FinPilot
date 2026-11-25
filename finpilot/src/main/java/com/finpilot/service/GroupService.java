package com.finpilot.service;

import com.finpilot.entity.Group;

import java.util.List;

public interface GroupService {

    Group createGroup(Group group);

    List<Group> getAllGroups(Long userId, boolean isAdmin);

    Group getGroupById(Long groupId, Long userId, boolean isAdmin);

    Group updateGroup(Long groupId, Group group, Long userId, boolean isAdmin);

    void deleteGroup(Long groupId, Long userId, boolean isAdmin);
}