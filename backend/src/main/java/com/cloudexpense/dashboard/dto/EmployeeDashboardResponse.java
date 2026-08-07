package com.cloudexpense.dashboard.dto;

import java.math.BigDecimal;

/**
 * ClassName: EmployeeDashboardResponse
 * Package: com.cloudexpense.dashboard.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/5 20:34
 * @Version: v1.0
 */
public record EmployeeDashboardResponse(

        long totalCount,

        BigDecimal totalAmount,

        long draftCount,

        long pendingApprovalCount,

        long approvedCount,

        long paidCount,

        long rejectedCount

) {
}
