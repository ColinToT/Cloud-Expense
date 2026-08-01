package com.cloudexpense.expense.dto;

import com.cloudexpense.receipt.dto.ReceiptResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * ClassName: ExpenseDetailResponse
 * Package: com.cloudexpense.expense.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/1 20:12
 * @Version: v1.0
 */
public record ExpenseDetailResponse(
        Long id,

        String title,

        String description,

        BigDecimal amount,

        String currency,

        Long categoryId,

        LocalDate expenseDate,

        String status,

        OffsetDateTime submittedAt,

        List<ReceiptResponse> receipts
) {
}
