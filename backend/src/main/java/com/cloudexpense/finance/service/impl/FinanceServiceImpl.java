package com.cloudexpense.finance.service.impl;

import com.cloudexpense.approval.dto.ApprovalHistoryResponse;
import com.cloudexpense.approval.entity.ApprovalRecord;
import com.cloudexpense.approval.repository.ApprovalRecordRepository;
import com.cloudexpense.common.exception.ResourceNotFoundException;
import com.cloudexpense.expense.entity.Expense;
import com.cloudexpense.expense.repository.ExpenseRepository;
import com.cloudexpense.finance.dto.FinanceExpenseDetailResponse;
import com.cloudexpense.finance.dto.FinanceExpenseResponse;
import com.cloudexpense.finance.service.FinanceService;
import com.cloudexpense.receipt.dto.ReceiptResponse;
import com.cloudexpense.receipt.entity.Receipt;
import com.cloudexpense.receipt.repository.ReceiptRepository;
import com.cloudexpense.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: FinanceServiceImpl
 * Package: com.cloudexpense.finance.service.impl
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 20:11
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final ExpenseRepository expenseRepository;
    private final ReceiptRepository receiptRepository;
    private final ApprovalRecordRepository approvalRecordRepository;

    @Override
    public List<FinanceExpenseResponse> getExpenses() {
        return expenseRepository
                .findByDeletedAtIsNullOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FinanceExpenseDetailResponse getExpenseDetail(Long id) {
        Expense expense =
                expenseRepository
                        .findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Expense not found"
                                )
                        );

        List<ReceiptResponse> receipts =
                receiptRepository
                        .findAllByExpenseId(expense.getId())
                        .stream()
                        .map(this::toReceiptResponse)
                        .toList();

        List<ApprovalHistoryResponse> history =
                approvalRecordRepository
                        .findByExpenseIdOrderByCreatedAtDesc(
                                expense.getId()
                        )
                        .stream()
                        .map(this::toHistoryResponse)
                        .toList();

        return toDetailResponse(expense, receipts, history);
    }

    private ApprovalHistoryResponse toHistoryResponse(ApprovalRecord record){
        User approver = record.getApprover();

        return new ApprovalHistoryResponse(
                record.getApprovalStage().name(),
                record.getAction().name(),
                approver.getFirstName()
                        + " "
                        + approver.getLastName(),
                record.getComment(),
                record.getCreatedAt()
        );
    }

    private ReceiptResponse toReceiptResponse(Receipt receipt){
        return new ReceiptResponse(
                receipt.getId(),
                receipt.getFileName(),
                receipt.getFileUrl(),
                receipt.getUploadedAt()
        );
    }

    private FinanceExpenseDetailResponse toDetailResponse(
            Expense expense,
            List<ReceiptResponse> receipts,
            List<ApprovalHistoryResponse> history
    ){
        User employee = expense.getUser();

        return new FinanceExpenseDetailResponse(
                expense.getId(),
                employee.getFirstName()
                        + " "
                        + employee.getLastName(),
                employee.getEmail(),
                expense.getTitle(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCurrency(),
                expense.getCategoryId(),
                expense.getExpenseDate(),
                expense.getStatus().name(),
                expense.getSubmittedAt(),
                receipts,
                history
        );
    }

    private FinanceExpenseResponse toResponse(Expense expense){
        return new FinanceExpenseResponse(
                expense.getId(),
                expense.getUser()
                        .getFirstName()
                        +
                        " "
                        +
                        expense.getUser().getLastName(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getCurrency(),
                expense.getStatus().name(),
                expense.getExpenseDate()
        );

    }
}
