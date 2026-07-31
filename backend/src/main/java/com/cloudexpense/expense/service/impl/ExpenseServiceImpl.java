package com.cloudexpense.expense.service.impl;

import com.cloudexpense.common.exception.BusinessException;
import com.cloudexpense.common.exception.ResourceNotFoundException;
import com.cloudexpense.expense.dto.CreateExpenseRequest;
import com.cloudexpense.expense.dto.ExpenseResponse;
import com.cloudexpense.expense.dto.UpdateExpenseRequest;
import com.cloudexpense.expense.entity.Expense;
import com.cloudexpense.expense.entity.ExpenseStatus;
import com.cloudexpense.expense.repository.ExpenseRepository;
import com.cloudexpense.expense.service.ExpenseService;
import com.cloudexpense.receipt.repository.ReceiptRepository;
import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * ClassName: ExpenseServiceImpl
 * Package: com.cloudexpense.expense.service.impl
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/28 20:50
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final CurrentUserService currentUserService;

    private final ReceiptRepository receiptRepository;

    @Override
    public ExpenseResponse createExpense(CreateExpenseRequest request) {

        User user = currentUserService.getCurrentUser();

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setTitle(request.title());
        expense.setDescription(request.description());
        expense.setAmount(request.amount());
        expense.setCurrency(
                request.currency() == null
                        ? "EUR"
                        : request.currency()
        );
        expense.setCategoryId(request.categoryId());
        expense.setExpenseDate(request.expenseDate());
        expense.setStatus(ExpenseStatus.DRAFT);

        Expense saved = expenseRepository.save(expense);

        return toResponse(saved);
    }

    @Override
    public ExpenseResponse getExpense(Long id) {
        User currentUser = currentUserService.getCurrentUser();

        Expense expense =
                expenseRepository
                        .findByIdAndUserIdAndDeletedAtIsNull(
                                id,
                                currentUser.getId()
                        ).orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Expense not found"
                                )
                        );

        return toResponse(expense);
    }

    @Override
    public ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        Expense expense =
                expenseRepository
                        .findByIdAndUserIdAndDeletedAtIsNull(
                                id,
                                currentUser.getId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Expense not found"
                                )
                        );


        if (expense.getStatus() != ExpenseStatus.DRAFT &&
                expense.getStatus() != ExpenseStatus.REJECTED) {

            throw new BusinessException(
                    "Only draft or rejected expense can be updated"
            );
        }

        expense.setTitle(request.title());
        expense.setDescription(request.description());
        expense.setAmount(request.amount());
        expense.setCurrency(
                request.currency() == null
                        ? expense.getCurrency()
                        : request.currency()
        );
        expense.setCategoryId(request.categoryId());
        expense.setExpenseDate(request.expenseDate());

        Expense updated = expenseRepository.save(expense);

        return toResponse(updated);
    }

    @Override
    public ExpenseResponse submitExpense(Long id) {
        User currentUser = currentUserService.getCurrentUser();

        Expense expense =
                expenseRepository
                        .findByIdAndUserIdAndDeletedAtIsNull(
                                id,
                                currentUser.getId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Expense not found"
                                )
                        );

        validateExpense(expense);

        expense.setStatus(ExpenseStatus.SUBMITTED);
        expense.setSubmittedAt(OffsetDateTime.now());

        Expense submitted = expenseRepository.save(expense);
        return toResponse(submitted);
    }

    private void validateExpense(Expense expense) {
        if (expense.getStatus() != ExpenseStatus.DRAFT
                && expense.getStatus() != ExpenseStatus.REJECTED) {
            throw new BusinessException(
                    "Expense cannot be submitted"
            );
        }

        long receiptCount = receiptRepository.countByExpenseId(expense.getId());

        if (receiptCount == 0) {
            throw new BusinessException("Receipt is required");
        }
    }

    @Override
    public List<ExpenseResponse> getMyExpenses() {
        User currentUser = currentUserService.getCurrentUser();

        return expenseRepository
                .findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User currentUser = currentUserService.getCurrentUser();

        Expense expense =
                expenseRepository
                        .findByIdAndUserIdAndDeletedAtIsNull(
                                id,
                                currentUser.getId()
                        )
                        .orElseThrow(
                                () -> new BusinessException(
                                        "Expense not found"
                                )
                        );

        if (expense.getStatus() != ExpenseStatus.DRAFT) {
            throw new BusinessException("Only draft expense can be deleted");
        }

        receiptRepository.deleteByExpenseId(id);

        expense.setDeletedAt(OffsetDateTime.now());

        expenseRepository.save(expense);
    }

    private ExpenseResponse toResponse(Expense expense) {

        return new ExpenseResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCurrency(),
                expense.getCategoryId(),
                expense.getExpenseDate(),
                expense.getStatus().name()
        );
    }
}
