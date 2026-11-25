package com.finpilot.controller;

import com.finpilot.dto.request.GroupRequest;
import com.finpilot.dto.response.GroupResponse;
import com.finpilot.entity.Group;
import com.finpilot.entity.User;
import com.finpilot.mapper.GroupMapper;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService service;
    private final SecurityUtils securityUtils;

    public GroupController(GroupService service,
                           SecurityUtils securityUtils) {
        this.service = service;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public GroupResponse createGroup(@Valid @RequestBody GroupRequest request) {

        User currentUser = securityUtils.getCurrentUser();

        Group group = GroupMapper.toEntity(request, currentUser);

        return GroupMapper.toResponse(
                service.createGroup(group)
        );
    }

    @GetMapping
    public List<GroupResponse> getAllGroups() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return service.getAllGroups(currentUser.getUserId(), isAdmin)
                .stream()
                .map(GroupMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GroupResponse getGroupById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return GroupMapper.toResponse(
                service.getGroupById(id, currentUser.getUserId(), isAdmin)
        );
    }

    @PutMapping("/{id}")
    public GroupResponse updateGroup(
            @PathVariable Long id,
            @Valid @RequestBody GroupRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Group group = GroupMapper.toEntity(
                request,
                currentUser
        );

        return GroupMapper.toResponse(
                service.updateGroup(id, group, currentUser.getUserId(), isAdmin)
        );
    }

    @DeleteMapping("/{id}")
    public String deleteGroup(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        service.deleteGroup(id, currentUser.getUserId(), isAdmin);
        return "Group deleted successfully";
    }
}