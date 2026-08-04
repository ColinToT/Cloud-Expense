package com.cloudexpense.common.event;

/**
 * ClassName: ExpenseSubmittedEvent
 * Package: com.cloudexpense.common.event
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/4 20:43
 * @Version: v1.0
 */
public record ExpenseSubmittedEvent(

        Long expenseId,

        Long managerId

) {
}
