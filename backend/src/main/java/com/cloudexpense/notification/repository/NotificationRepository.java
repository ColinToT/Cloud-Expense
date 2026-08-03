package com.cloudexpense.notification.repository;

import com.cloudexpense.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ClassName: NotificationRepository
 * Package: com.cloudexpense.notification.repository
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/3 21:28
 * @Version: v1.0
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndReadStatusFalse(Long userId);

}
