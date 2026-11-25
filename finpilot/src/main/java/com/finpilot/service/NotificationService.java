package com.finpilot.service;

import com.finpilot.entity.Notification;

import java.util.List;

public interface NotificationService {

    Notification createNotification(Notification notification);

    List<Notification> getAllNotifications(Long userId, boolean isAdmin);

    Notification getNotificationById(Long id, Long userId, boolean isAdmin);

    Notification updateNotification(Long id, Notification notification, Long userId, boolean isAdmin);

    void deleteNotification(Long id, Long userId, boolean isAdmin);
}