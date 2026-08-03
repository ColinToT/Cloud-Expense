package com.cloudexpense.notification.service.impl;

import com.cloudexpense.common.exception.BusinessException;
import com.cloudexpense.notification.dto.NotificationResponse;
import com.cloudexpense.notification.entity.Notification;
import com.cloudexpense.notification.repository.NotificationRepository;
import com.cloudexpense.notification.service.NotificationService;
import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: NotificationServiceImpl
 * Package: com.cloudexpense.notification.service.impl
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/3 21:30
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final CurrentUserService currentUserService;

    @Override
    public List<NotificationResponse> getMyNotifications() {
        User user = currentUserService.getCurrentUser();

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(
                        user.getId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void markAsRead(Long id) {
        User user = currentUserService.getCurrentUser();

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new BusinessException(
                                        "Notification not found"
                                )
                        );

        if(!notification.getUser()
                .getId()
                .equals(user.getId())){

            throw new BusinessException(
                    "Cannot access this notification"
            );
        }

        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }

    @Override
    public long getUnreadCount() {
        User user = currentUserService.getCurrentUser();

        return notificationRepository
                .countByUserIdAndReadStatusFalse(
                        user.getId()
                );
    }

    private NotificationResponse toResponse(Notification notification){
        return new NotificationResponse(
                notification.getId(),
                notification.getType().name(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getReferenceId(),
                notification.getReadStatus(),
                notification.getCreatedAt()
        );
    }
}
