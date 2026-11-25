package com.finpilot.service.impl;

import com.finpilot.entity.Group;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.GroupRepository;
import com.finpilot.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public List<Group> getAllGroups(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return groupRepository.findAll();
        }
        return groupRepository.findGroupsByUserIdOrMemberUserId(userId);
    }

    @Override
    public Group getGroupById(Long groupId, Long userId, boolean isAdmin) {
        if (isAdmin) {
            return groupRepository.findById(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        }
        return groupRepository.findByGroupIdAndUserOrMember(groupId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
    }

    @Override
    public Group updateGroup(Long groupId, Group group, Long userId, boolean isAdmin) {

        Group existing = getGroupById(groupId, userId, isAdmin);

        existing.setGroupName(group.getGroupName());
        existing.setGroupType(group.getGroupType());
        existing.setDescription(group.getDescription());
        existing.setIsActive(group.getIsActive());

        return groupRepository.save(existing);
    }

    @Override
    public void deleteGroup(Long groupId, Long userId, boolean isAdmin) {
        Group existing = getGroupById(groupId, userId, isAdmin);
        groupRepository.delete(existing);
    }
}