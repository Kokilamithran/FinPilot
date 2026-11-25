package com.finpilot.service.impl;

import com.finpilot.entity.GroupMember;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.GroupMemberRepository;
import com.finpilot.service.GroupMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository repository;

    public GroupMemberServiceImpl(GroupMemberRepository repository) {
        this.repository = repository;
    }

    @Override
    public GroupMember createGroupMember(GroupMember member) {
        return repository.save(member);
    }

    @Override
    public List<GroupMember> getAllGroupMembers(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findAll();
        }
        return repository.findByUser_UserId(userId);
    }

    @Override
    public GroupMember getGroupMemberById(Long id, Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Group Member not found"));
        }
        return repository.findByGroupMemberIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Group Member not found"));
    }

    @Override
    public GroupMember updateGroupMember(Long id, GroupMember member, Long userId, boolean isAdmin) {

        GroupMember existing = getGroupMemberById(id, userId, isAdmin);

        existing.setMemberRole(member.getMemberRole());

        return repository.save(existing);
    }

    @Override
    public void deleteGroupMember(Long id, Long userId, boolean isAdmin) {
        GroupMember existing = getGroupMemberById(id, userId, isAdmin);
        repository.delete(existing);
    }
}