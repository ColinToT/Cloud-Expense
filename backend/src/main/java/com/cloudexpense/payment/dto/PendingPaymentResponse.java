package com.cloudexpense.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ClassName: PendingPaymentResponse
 * Package: com.cloudexpense.payment.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 20:54
 * @Version: v1.0
 */
public record PendingPaymentResponse(
        Long expenseId,

        String employeeName,

        String title,

        BigDecimal amount,

        String currency,

        LocalDate expenseDate
) {
}
