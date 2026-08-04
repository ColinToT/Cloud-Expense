package com.cloudexpense.common.event;

/**
 * ClassName: ExpenseRejectedEvent
 * Package: com.cloudexpense.common.event
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/4 20:50
 * @Version: v1.0
 */
public record ExpenseRejectedEvent(

        Long expenseId,

        Long employeeId,

        String comment

) {
}
