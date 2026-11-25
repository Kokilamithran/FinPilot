package com.finpilot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupRequest {

    @NotBlank
    private String groupName;

    private String groupType;

    private String description;

    private Boolean isActive;

    private Long createdBy;
}