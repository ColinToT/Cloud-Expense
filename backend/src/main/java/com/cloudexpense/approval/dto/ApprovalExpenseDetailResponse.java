package com.cloudexpense.approval.dto;

import com.cloudexpense.expense.entity.ExpenseStatus;
import com.cloudexpense.receipt.dto.ReceiptResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record ApprovalExpenseDetailResponse(
        Long id,
        String employeeName,
        String employeeEmail,
        String title,
        String description,
        BigDecimal amount,
        String currency,
        Long categoryId,
        LocalDate expenseDate,
        ExpenseStatus status,
        OffsetDateTime submittedAt,
        List<ReceiptResponse> receipts
) {}