package com.cloudexpense.report.service;

import com.cloudexpense.report.dto.ExpenseReportQuery;
import com.cloudexpense.report.dto.ExpenseReportResponse;

import java.util.List;

/**
 * ClassName: ReportService
 * Package: com.cloudexpense.report.service
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 21:16
 * @Version: v1.0
 */
public interface ReportService {
    List<ExpenseReportResponse> getExpenses(ExpenseReportQuery query);

    byte[] exportExpenses(ExpenseReportQuery query);
}
