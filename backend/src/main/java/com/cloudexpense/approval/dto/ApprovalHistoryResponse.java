package com.cloudexpense.approval.dto;

import java.time.OffsetDateTime;

/**
 * ClassName: ApprovalHistoryResponse
 * Package: com.cloudexpense.approval.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/1 21:45
 * @Version: v1.0
 */
public record ApprovalHistoryResponse(
        String stage,

        String action,

        String approverName,

        String comment,

        OffsetDateTime createdAt
) {
}
