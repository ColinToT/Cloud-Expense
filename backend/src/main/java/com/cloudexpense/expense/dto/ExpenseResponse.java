package com.cloudexpense.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ClassName: ExpenseResponse
 * Package: com.cloudexpense.expense.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/28 21:03
 * @Version: v1.0
 */
public record ExpenseResponse(
        Long id,

        String title,

        String description,

        BigDecimal amount,

        String currency,

        Long categoryId,

        LocalDate expenseDate,

        String status
) {
}
