package com.finpilot.repository;

import com.finpilot.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_UserId(Long userId);
    Optional<Notification> findByNotificationIdAndUser_UserId(Long notificationId, Long userId);
}