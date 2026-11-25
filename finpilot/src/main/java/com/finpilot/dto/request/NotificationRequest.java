package com.finpilot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationRequest {

    @NotBlank
    private String notificationType;

    @NotBlank
    private String message;

    private String status;

    private Long userId;
}