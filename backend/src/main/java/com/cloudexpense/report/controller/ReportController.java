package com.cloudexpense.report.controller;

import com.cloudexpense.expense.entity.ExpenseStatus;
import com.cloudexpense.report.dto.ExpenseReportQuery;
import com.cloudexpense.report.dto.ExpenseReportResponse;
import com.cloudexpense.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * ClassName: ReportController
 * Package: com.cloudexpense.report.controller
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 21:19
 * @Version: v1.0
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','FINANCE')")
    @GetMapping("/expenses")
    public List<ExpenseReportResponse> getExpenses(
            @RequestParam(required = false)
            LocalDate startDate,

            @RequestParam(required = false)
            LocalDate endDate,

            @RequestParam(required = false)
            ExpenseStatus status
    ) {
        return reportService.getExpenses(
                new ExpenseReportQuery(
                        startDate,
                        endDate,
                        status
                )
        );
    }

    @GetMapping("/expenses/export")
    public ResponseEntity<byte[]> exportExpenses(
            @RequestParam(required = false)
            LocalDate startDate,

            @RequestParam(required = false)
            LocalDate endDate,

            @RequestParam(required = false)
            ExpenseStatus status
    ) {
        byte[] data = reportService.exportExpenses(
                new ExpenseReportQuery(
                        startDate,
                        endDate,
                        status
                )
        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=expenses-report.xlsx"
                )
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}
