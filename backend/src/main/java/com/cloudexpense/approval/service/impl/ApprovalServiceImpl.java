package com.cloudexpense.approval.service.impl;

import com.cloudexpense.approval.dto.ApprovalHistoryResponse;
import com.cloudexpense.approval.dto.ApprovalRequest;
import com.cloudexpense.approval.dto.PendingApprovalResponse;
import com.cloudexpense.approval.entity.ApprovalAction;
import com.cloudexpense.approval.entity.ApprovalRecord;
import com.cloudexpense.approval.entity.ApprovalStage;
import com.cloudexpense.approval.repository.ApprovalRecordRepository;
import com.cloudexpense.approval.service.ApprovalService;
import com.cloudexpense.common.exception.BusinessException;
import com.cloudexpense.expense.entity.Expense;
import com.cloudexpense.expense.entity.ExpenseStatus;
import com.cloudexpense.expense.repository.ExpenseRepository;
import com.cloudexpense.user.entity.Role;
import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName: ApprovalServiceImpl
 * Package: com.cloudexpense.approval.service.impl
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/1 21:01
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ExpenseRepository expenseRepository;
    private final ApprovalRecordRepository approvalRecordRepository;
    private final CurrentUserService currentUserService;

    @Override
    public List<PendingApprovalResponse> getPendingExpenses() {
        User currentUser = currentUserService.getCurrentUser();
        ExpenseStatus pendingStatus = getPendingStatus(currentUser);

        if (currentUser.getRole() == Role.MANAGER) {
            return expenseRepository
                    .findByUserManagerIdAndStatusAndDeletedAtIsNull(
                            currentUser.getId(),
                            pendingStatus
                    )
                    .stream()
                    .map(this::toPendingResponse)
                    .toList();
        }

        return expenseRepository
                .findByStatusAndDeletedAtIsNull(pendingStatus)
                .stream()
                .map(this::toPendingResponse)
                .toList();
    }

    private ExpenseStatus getPendingStatus(User user) {
        return switch (user.getRole()) {
            case MANAGER -> ExpenseStatus.SUBMITTED;
            case FINANCE -> ExpenseStatus.MANAGER_APPROVED;
            default -> throw new BusinessException("User cannot approve expenses");
        };
    }

    @Override
    @Transactional
    public void approve(Long expenseId, ApprovalRequest request) {
        User approver = currentUserService.getCurrentUser();

        Expense expense = getPendingExpense(expenseId, approver);

        ApprovalStage stage = getApprovalStage(approver);
        expense.setStatus(getApprovedStatus(stage));
        expenseRepository.save(expense);

        saveApprovalRecord(
                expense,
                approver,
                stage,
                ApprovalAction.APPROVE,
                request.comment()
        );
    }

    private ExpenseStatus getApprovedStatus(ApprovalStage stage) {
        return switch (stage) {
            case MANAGER -> ExpenseStatus.MANAGER_APPROVED;
            case FINANCE -> ExpenseStatus.FINANCE_APPROVED;
        };
    }

    private ApprovalStage getApprovalStage(User user) {
        return switch (user.getRole()) {
            case MANAGER -> ApprovalStage.MANAGER;
            case FINANCE -> ApprovalStage.FINANCE;
            default -> throw new BusinessException("Invalid approval role");
        };
    }

    @Override
    @Transactional
    public void reject(Long expenseId, ApprovalRequest request) {
        User approver = currentUserService.getCurrentUser();

        Expense expense = getPendingExpense(expenseId, approver);
        expense.setStatus(ExpenseStatus.REJECTED);
        expenseRepository.save(expense);

        ApprovalStage stage = getApprovalStage(approver);

        saveApprovalRecord(
                expense,
                approver,
                stage,
                ApprovalAction.REJECT,
                request.comment()
        );
    }

    @Override
    public List<ApprovalHistoryResponse> getApprovalHistory(Long expenseId) {
        List<ApprovalRecord> records =
                approvalRecordRepository
                        .findByExpenseIdOrderByCreatedAtDesc(
                                expenseId
                        );

        return records.stream()
                .map(this::toHistoryResponse)
                .toList();
    }

    private ApprovalHistoryResponse toHistoryResponse(ApprovalRecord record) {
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

    private void saveApprovalRecord(
            Expense expense,
            User approver,
            ApprovalStage stage,
            ApprovalAction action,
            String comment
    ) {
        ApprovalRecord record = new ApprovalRecord();
        record.setExpense(expense);
        record.setApprover(approver);
        record.setApprovalStage(stage);
        record.setAction(action);
        record.setComment(comment);
        approvalRecordRepository.save(record);
    }

    private Expense getPendingExpense(Long expenseId, User approver) {
        ExpenseStatus status = getPendingStatus(approver);

        if (approver.getRole() == Role.MANAGER) {

            return expenseRepository
                    .findByIdAndUserManagerIdAndStatusAndDeletedAtIsNull(
                            expenseId,
                            approver.getId(),
                            status
                    )
                    .orElseThrow(
                            () -> new BusinessException(
                                    "Expense cannot be approved"
                            )
                    );
        }

        return expenseRepository
                .findByIdAndStatusAndDeletedAtIsNull(
                        expenseId,
                        status
                )
                .orElseThrow(
                        () -> new BusinessException(
                                "Expense cannot be approved"
                        )
                );
    }

    private PendingApprovalResponse toPendingResponse(Expense expense) {
        User employee = expense.getUser();

        return new PendingApprovalResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getCurrency(),
                expense.getExpenseDate(),
                employee.getFirstName()
                        + " "
                        + employee.getLastName(),
                expense.getSubmittedAt()
        );
    }
}
