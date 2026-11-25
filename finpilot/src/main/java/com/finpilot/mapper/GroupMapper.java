package com.finpilot.mapper;

import com.finpilot.dto.request.GroupRequest;
import com.finpilot.dto.response.GroupResponse;
import com.finpilot.entity.Group;
import com.finpilot.entity.User;

public class GroupMapper {

    public static Group toEntity(GroupRequest request, User user) {

        Group group = new Group();

        group.setGroupName(request.getGroupName());
        group.setGroupType(request.getGroupType());
        group.setDescription(request.getDescription());
        group.setIsActive(request.getIsActive());
        group.setCreatedBy(user);

        return group;
    }

    public static GroupResponse toResponse(Group group) {

        GroupResponse response = new GroupResponse();

        response.setGroupId(group.getGroupId());
        response.setGroupName(group.getGroupName());
        response.setGroupType(group.getGroupType());
        response.setDescription(group.getDescription());
        response.setIsActive(group.getIsActive());
        response.setCreatedBy(group.getCreatedBy().getUserId());
        response.setCreatedDate(group.getCreatedDate());
        response.setModifiedDate(group.getModifiedDate());

        return response;
    }
}