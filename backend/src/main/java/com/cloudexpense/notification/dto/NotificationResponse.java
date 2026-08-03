package com.cloudexpense.notification.dto;

import java.time.OffsetDateTime;

/**
 * ClassName: NotificationResponse
 * Package: com.cloudexpense.notification.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/3 21:29
 * @Version: v1.0
 */
public record NotificationResponse(
        Long id,

        String type,

        String title,

        String message,

        Long referenceId,

        Boolean readStatus,

        OffsetDateTime createdAt
) {
}
