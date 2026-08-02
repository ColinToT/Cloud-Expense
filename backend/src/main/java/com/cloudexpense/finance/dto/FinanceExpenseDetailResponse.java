package com.cloudexpense.finance.dto;

import com.cloudexpense.approval.dto.ApprovalHistoryResponse;
import com.cloudexpense.receipt.dto.ReceiptResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * ClassName: FinanceExpenseDetailResponse
 * Package: com.cloudexpense.finance.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 20:21
 * @Version: v1.0
 */
public record FinanceExpenseDetailResponse(
        Long id,

        String employeeName,

        String employeeEmail,

        String title,

        String description,

        BigDecimal amount,

        String currency,

        Long categoryId,

        LocalDate expenseDate,

        String status,

        OffsetDateTime submittedAt,

        List<ReceiptResponse> receipts,

        List<ApprovalHistoryResponse> approvalHistory
) {
}
