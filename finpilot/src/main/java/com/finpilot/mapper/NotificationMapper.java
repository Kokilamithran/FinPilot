package com.finpilot.mapper;

import com.finpilot.dto.request.NotificationRequest;
import com.finpilot.dto.response.NotificationResponse;
import com.finpilot.entity.Notification;
import com.finpilot.entity.User;

public class NotificationMapper {

    public static Notification toEntity(NotificationRequest request, User user) {

        Notification notification = new Notification();

        notification.setNotificationType(request.getNotificationType());
        notification.setMessage(request.getMessage());
        notification.setStatus(request.getStatus());
        notification.setUser(user);

        return notification;
    }

    public static NotificationResponse toResponse(Notification notification) {

        NotificationResponse response = new NotificationResponse();

        response.setNotificationId(notification.getNotificationId());
        response.setNotificationType(notification.getNotificationType());
        response.setMessage(notification.getMessage());
        response.setStatus(notification.getStatus());
        response.setUserId(notification.getUser().getUserId());
        response.setCreatedDate(notification.getCreatedDate());
        response.setReadDate(notification.getReadDate());

        return response;
    }
}