package com.finpilot.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupMemberResponse {

    private Long groupMemberId;

    private Long groupId;

    private Long userId;

    private String memberRole;

    private LocalDateTime joinedDate;
}