package com.cloudexpense.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ClassName: UpdateExpenseRequest
 * Package: com.cloudexpense.expense.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/28 21:47
 * @Version: v1.0
 */
public record UpdateExpenseRequest(
        String title,

        String description,

        BigDecimal amount,

        String currency,

        Long categoryId,

        LocalDate expenseDate
) {
}
