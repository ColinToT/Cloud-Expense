package com.cloudexpense.report.dto;

import com.cloudexpense.expense.entity.ExpenseStatus;

import java.time.LocalDate;

/**
 * ClassName: ExpenseReportQuery
 * Package: com.cloudexpense.report.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 21:09
 * @Version: v1.0
 */
public record ExpenseReportQuery(

        LocalDate startDate,

        LocalDate endDate,

        ExpenseStatus status

) {
}
