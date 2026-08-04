package com.cloudexpense.common.event;

import java.math.BigDecimal;

/**
 * ClassName: PaymentCompletedEvent
 * Package: com.cloudexpense.common.event
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/4 21:47
 * @Version: v1.0
 */
public record PaymentCompletedEvent(

        Long expenseId,

        Long employeeId,

        BigDecimal amount

) {
}
