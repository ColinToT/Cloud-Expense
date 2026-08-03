package com.cloudexpense.notification.controller;

import com.cloudexpense.notification.dto.NotificationResponse;
import com.cloudexpense.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: NotificationController
 * Package: com.cloudexpense.notification.controller
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/3 21:34
 * @Version: v1.0
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(){
        return ResponseEntity.ok(
                notificationService.getMyNotifications()
        );
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long id){
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/unread-count")
    public ResponseEntity<Long> unreadCount(){
        return ResponseEntity.ok(
                notificationService.getUnreadCount()
        );
    }
}
