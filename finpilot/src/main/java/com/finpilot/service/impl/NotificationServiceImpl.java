package com.finpilot.service.impl;

import com.finpilot.entity.Notification;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.NotificationRepository;
import com.finpilot.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification createNotification(Notification notification) {
        return repository.save(notification);
    }

    @Override
    public List<Notification> getAllNotifications(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findAll();
        }
        return repository.findByUser_UserId(userId);
    }

    @Override
    public Notification getNotificationById(Long id, Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        }
        return repository.findByNotificationIdAndUser_UserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
    }

    @Override
    public Notification updateNotification(Long id, Notification notification, Long userId, boolean isAdmin) {

        Notification existing = getNotificationById(id, userId, isAdmin);

        if (notification.getNotificationType() != null) {
            existing.setNotificationType(notification.getNotificationType());
        }
        if (notification.getMessage() != null) {
            existing.setMessage(notification.getMessage());
        }
        if (notification.getStatus() != null) {
            if ("READ".equalsIgnoreCase(notification.getStatus())) {
                if (!"READ".equalsIgnoreCase(existing.getStatus()) || existing.getReadDate() == null) {
                    existing.setReadDate(LocalDateTime.now());
                }
            } else if ("UNREAD".equalsIgnoreCase(notification.getStatus())) {
                existing.setReadDate(null);
            }
            existing.setStatus(notification.getStatus());
        }

        return repository.save(existing);
    }

    @Override
    public void deleteNotification(Long id, Long userId, boolean isAdmin) {
        Notification existing = getNotificationById(id, userId, isAdmin);
        repository.delete(existing);
    }
}