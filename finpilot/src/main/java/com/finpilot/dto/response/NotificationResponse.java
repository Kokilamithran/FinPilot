package com.finpilot.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private Long notificationId;

    private String notificationType;

    private String message;

    private String status;

    private Long userId;

    private LocalDateTime createdDate;

    private LocalDateTime readDate;
}