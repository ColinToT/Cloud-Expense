package com.cloudexpense.dashboard.dto;

import java.math.BigDecimal;

/**
 * ClassName: ManagerDashboardResponse
 * Package: com.cloudexpense.dashboard.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/5 21:29
 * @Version: v1.0
 */
public record ManagerDashboardResponse(

        long pendingApprovalCount,

        BigDecimal pendingApprovalAmount,

        long approvedCount,

        long rejectedCount

) {
}
