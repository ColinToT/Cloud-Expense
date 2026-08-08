package com.cloudexpense.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ClassName: ExpenseReportResponse
 * Package: com.cloudexpense.report.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 21:08
 * @Version: v1.0
 */
public record ExpenseReportResponse(

        Long expenseId,

        String title,

        String employeeName,

        BigDecimal amount,

        String currency,

        String status,

        LocalDate expenseDate

) {
}
