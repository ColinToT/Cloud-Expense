package com.cloudexpense.finance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ClassName: FinanceExpenseResponse
 * Package: com.cloudexpense.finance.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 20:09
 * @Version: v1.0
 */
public record FinanceExpenseResponse(
        Long id,

        String employeeName,

        String title,

        BigDecimal amount,

        String currency,

        String status,

        LocalDate expenseDate
) {
}
