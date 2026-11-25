package com.finpilot.service;

import com.finpilot.entity.Notification;
import com.finpilot.entity.User;
import com.finpilot.repository.NotificationRepository;
import com.finpilot.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification unreadNotification;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserId(5L);

        unreadNotification = new Notification();
        unreadNotification.setNotificationId(1L);
        unreadNotification.setUser(user);
        unreadNotification.setNotificationType("ALERT");
        unreadNotification.setMessage("Budget warning");
        unreadNotification.setStatus("UNREAD");
        unreadNotification.setCreatedDate(LocalDateTime.now().minusHours(2));
        unreadNotification.setReadDate(null);
    }

    @Test
    void updateNotification_whenChangingToRead_setsReadDateTimestamp() {
        when(repository.findByNotificationIdAndUser_UserId(1L, 5L))
                .thenReturn(Optional.of(unreadNotification));
        when(repository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Notification incoming = new Notification();
        incoming.setNotificationType("ALERT");
        incoming.setMessage("Budget warning");
        incoming.setStatus("READ");

        Notification updated = notificationService.updateNotification(1L, incoming, 5L, false);

        assertEquals("READ", updated.getStatus());
        assertNotNull(updated.getReadDate());
    }

    @Test
    void updateNotification_whenAlreadyRead_preservesExistingReadDate() {
        LocalDateTime originalReadDate = LocalDateTime.now().minusHours(1);
        unreadNotification.setStatus("READ");
        unreadNotification.setReadDate(originalReadDate);

        when(repository.findByNotificationIdAndUser_UserId(1L, 5L))
                .thenReturn(Optional.of(unreadNotification));
        when(repository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Notification incoming = new Notification();
        incoming.setStatus("READ");

        Notification updated = notificationService.updateNotification(1L, incoming, 5L, false);

        assertEquals("READ", updated.getStatus());
        assertEquals(originalReadDate, updated.getReadDate());
    }
}
