package com.finpilot.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupResponse {

    private Long groupId;

    private String groupName;

    private String groupType;

    private String description;

    private Boolean isActive;

    private Long createdBy;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}