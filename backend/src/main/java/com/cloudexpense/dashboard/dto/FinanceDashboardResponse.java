package com.cloudexpense.dashboard.dto;

import java.math.BigDecimal;

/**
 * ClassName: FinanceDashboardResponse
 * Package: com.cloudexpense.dashboard.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 20:50
 * @Version: v1.0
 */
public record FinanceDashboardResponse(

        long pendingApprovalCount,

        BigDecimal pendingApprovalAmount,

        long pendingPaymentCount,

        BigDecimal pendingPaymentAmount,

        long paidCount,

        BigDecimal paidAmount

) {
}
