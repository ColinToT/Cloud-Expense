package com.cloudexpense.common.event;

import com.cloudexpense.approval.entity.ApprovalStage;

/**
 * ClassName: ExpenseApprovedEvent
 * Package: com.cloudexpense.common.event
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/4 20:12
 * @Version: v1.0
 */
public record ExpenseApprovedEvent(

        Long expenseId,

        Long employeeId,

        ApprovalStage stage

) {
}
