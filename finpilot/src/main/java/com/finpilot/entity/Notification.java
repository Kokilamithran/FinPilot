package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false, length = 50)
    private String notificationType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false, length = 20)
    private String status = "UNREAD";

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime readDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();

        if (status == null) {
            status = "UNREAD";
        }
    }
}