package com.cloudexpense.notification.service;

import com.cloudexpense.notification.dto.NotificationResponse;
import com.cloudexpense.notification.entity.NotificationType;

import java.util.List;

/**
 * ClassName: NotificationService
 * Package: com.cloudexpense.notification.service
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/3 21:30
 * @Version: v1.0
 */
public interface NotificationService {

    List<NotificationResponse> getMyNotifications();

    void markAsRead(Long id);

    long getUnreadCount();

    void create(
            Long userId,
            NotificationType type,
            String title,
            String message,
            Long referenceId
    );
}
