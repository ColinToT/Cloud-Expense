package com.cloudexpense.report.service.impl;

import com.cloudexpense.common.exception.BusinessException;
import com.cloudexpense.report.dto.ExpenseReportQuery;
import com.cloudexpense.report.dto.ExpenseReportResponse;
import com.cloudexpense.report.repository.ReportRepository;
import com.cloudexpense.report.repository.projection.ExpenseReportProjection;
import com.cloudexpense.report.service.ReportService;
import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * ClassName: ReportServiceImpl
 * Package: com.cloudexpense.report.service.impl
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 21:17
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final CurrentUserService currentUserService;
    private final ReportRepository reportRepository;

    @Override
    public List<ExpenseReportResponse> getExpenses(ExpenseReportQuery query) {
        User user = currentUserService.getCurrentUser();
        List<ExpenseReportProjection> result;
        String status = query.status() == null ? null : query.status().name();

        result = switch (user.getRole()) {
            case EMPLOYEE -> reportRepository.findEmployeeReports(
                    user.getId(),
                    query.startDate(),
                    query.endDate(),
                    status
            );

            case MANAGER -> reportRepository.findManagerReports(
                    user.getId(),
                    query.startDate(),
                    query.endDate(),
                    status
            );

            case FINANCE -> reportRepository.findAllReports(
                    query.startDate(),
                    query.endDate(),
                    status
            );

            default -> throw new BusinessException(
                    "Invalid role"
            );

        };

        return result.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public byte[] exportExpenses(ExpenseReportQuery query) {
        List<ExpenseReportResponse> data = getExpenses(query);

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {

            Sheet sheet = workbook.createSheet("Expenses");
            CellStyle headerStyle = createHeaderStyle(workbook);
            createHeader(sheet, headerStyle);
            sheet.createFreezePane(0,1);
            int rowIndex = 1;
            for (
                    ExpenseReportResponse expense : data
            ) {

                Row row = sheet.createRow(rowIndex++);
                row.createCell(0)
                        .setCellValue(
                                expense.expenseId()
                        );

                row.createCell(1)
                        .setCellValue(
                                expense.title()
                        );

                row.createCell(2)
                        .setCellValue(
                                expense.employeeName()
                        );

                row.createCell(3)
                        .setCellValue(
                                expense.amount()
                                        .doubleValue()
                        );

                row.createCell(4)
                        .setCellValue(
                                expense.currency()
                        );

                row.createCell(5)
                        .setCellValue(
                                expense.status()
                        );

                row.createCell(6)
                        .setCellValue(
                                expense.expenseDate()
                                        .toString()
                        );
            }
            autoSize(sheet);
            workbook.write(output);
            return output.toByteArray();

        } catch (IOException e) {

            throw new BusinessException(
                    "Failed to generate report"
            );

        }
    }

    private void autoSize(Sheet sheet) {
        for (int column = 0; column < 7; column++) {
            int maxLength = 0;
            for (Row row : sheet) {
                Cell cell = row.getCell(column);
                if (cell != null) {
                    int length = cell.toString().length();
                    maxLength = Math.max(maxLength, length);
                }
            }
            int width = (maxLength + 3) * 256;
            width = Math.min(width, 60 * 256);
            sheet.setColumnWidth(column, width);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private void createHeader(Sheet sheet, CellStyle style) {
        Row row = sheet.createRow(0);
        String[] headers = {
                "Expense ID",
                "Title",
                "Employee",
                "Amount",
                "Currency",
                "Status",
                "Date"
        };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private ExpenseReportResponse toResponse(ExpenseReportProjection projection) {
        return new ExpenseReportResponse(
                projection.getExpenseId(),
                projection.getTitle(),
                projection.getEmployeeName(),
                projection.getAmount(),
                projection.getCurrency(),
                projection.getStatus(),
                projection.getExpenseDate()
        );
    }

}
