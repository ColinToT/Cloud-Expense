package com.cloudexpense.approval.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * ClassName: PendingApprovalResponse
 * Package: com.cloudexpense.approval.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/1 21:00
 * @Version: v1.0
 */
public record PendingApprovalResponse(

        Long expenseId,

        String title,

        BigDecimal amount,

        String currency,

        LocalDate expenseDate,

        String employeeName,

        OffsetDateTime submittedAt

) {}
