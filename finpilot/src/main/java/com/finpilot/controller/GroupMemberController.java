package com.finpilot.controller;

import com.finpilot.dto.request.GroupMemberRequest;
import com.finpilot.dto.response.GroupMemberResponse;
import com.finpilot.entity.Group;
import com.finpilot.entity.GroupMember;
import com.finpilot.entity.User;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.mapper.GroupMemberMapper;
import com.finpilot.repository.GroupRepository;
import com.finpilot.repository.UserRepository;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.GroupMemberService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/group-members")
public class GroupMemberController {

    private final GroupMemberService service;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public GroupMemberController(GroupMemberService service,
                                 GroupRepository groupRepository,
                                 UserRepository userRepository,
                                 SecurityUtils securityUtils) {
        this.service = service;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public GroupMemberResponse createGroupMember(@Valid @RequestBody GroupMemberRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Group group;
        if (isAdmin) {
            group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        } else {
            group = groupRepository.findByGroupIdAndCreatedBy_UserId(request.getGroupId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        GroupMember member = GroupMemberMapper.toEntity(request, group, user);

        return GroupMemberMapper.toResponse(
                service.createGroupMember(member)
        );
    }

    @GetMapping
    public List<GroupMemberResponse> getAllGroupMembers() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return service.getAllGroupMembers(currentUser.getUserId(), isAdmin)
                .stream()
                .map(GroupMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GroupMemberResponse getGroupMemberById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return GroupMemberMapper.toResponse(
                service.getGroupMemberById(id, currentUser.getUserId(), isAdmin)
        );
    }

    @PutMapping("/{id}")
    public GroupMemberResponse updateGroupMember(
            @PathVariable Long id,
            @Valid @RequestBody GroupMemberRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Group group;
        if (isAdmin) {
            group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        } else {
            group = groupRepository.findByGroupIdAndCreatedBy_UserId(request.getGroupId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        GroupMember member = GroupMemberMapper.toEntity(
                request,
                group,
                user
        );

        return GroupMemberMapper.toResponse(
                service.updateGroupMember(id, member, currentUser.getUserId(), isAdmin)
        );
    }

    @DeleteMapping("/{id}")
    public String deleteGroupMember(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        service.deleteGroupMember(id, currentUser.getUserId(), isAdmin);
        return "Group Member deleted successfully";
    }
}