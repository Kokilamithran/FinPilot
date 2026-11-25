package com.finpilot.controller;

import com.finpilot.dto.request.NotificationRequest;
import com.finpilot.dto.response.NotificationResponse;
import com.finpilot.entity.Notification;
import com.finpilot.entity.User;
import com.finpilot.mapper.NotificationMapper;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;
    private final SecurityUtils securityUtils;

    public NotificationController(NotificationService service,
                                  SecurityUtils securityUtils) {
        this.service = service;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public NotificationResponse createNotification(@Valid @RequestBody NotificationRequest request) {

        User currentUser = securityUtils.getCurrentUser();

        Notification notification = NotificationMapper.toEntity(request, currentUser);

        return NotificationMapper.toResponse(
                service.createNotification(notification)
        );
    }

    @GetMapping
    public List<NotificationResponse> getAllNotifications() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return service.getAllNotifications(currentUser.getUserId(), isAdmin)
                .stream()
                .map(NotificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public NotificationResponse getNotificationById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return NotificationMapper.toResponse(
                service.getNotificationById(id, currentUser.getUserId(), isAdmin)
        );
    }

    @PutMapping("/{id}")
    public NotificationResponse updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody NotificationRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Notification notification = NotificationMapper.toEntity(
                request,
                currentUser
        );

        return NotificationMapper.toResponse(
                service.updateNotification(id, notification, currentUser.getUserId(), isAdmin)
        );
    }

    @DeleteMapping("/{id}")
    public String deleteNotification(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        service.deleteNotification(id, currentUser.getUserId(), isAdmin);
        return "Notification deleted successfully";
    }
}