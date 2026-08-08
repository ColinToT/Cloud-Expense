package com.cloudexpense.report.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ClassName: ExpenseReportProjection
 * Package: com.cloudexpense.report.repository.projection
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 21:10
 * @Version: v1.0
 */
public interface ExpenseReportProjection {
    Long getExpenseId();

    String getTitle();

    String getEmployeeName();

    BigDecimal getAmount();

    String getCurrency();

    String getStatus();

    LocalDate getExpenseDate();
}
