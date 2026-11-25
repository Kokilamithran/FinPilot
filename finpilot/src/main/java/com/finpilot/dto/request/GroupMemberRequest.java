package com.finpilot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupMemberRequest {

    @NotNull
    private Long groupId;

    @NotNull
    private Long userId;

    private String memberRole;
}