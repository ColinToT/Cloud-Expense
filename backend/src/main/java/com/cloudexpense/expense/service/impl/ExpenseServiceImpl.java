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
import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                        .findByIdAndUserId(
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
                        .findByIdAndUserId(
                                id,
                                currentUser.getId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Expense not found"
                                )
                        );


        if(expense.getStatus() != ExpenseStatus.DRAFT &&
                expense.getStatus() != ExpenseStatus.REJECTED){

            throw new BusinessException(
                    "Only draft or rejected expense can be updated"
            );
        }

        expense.setTitle(request.title());
        expense.setDescription(request.description());
        expense.setAmount(request.amount());
        expense.setCurrency(request.currency());
        expense.setCategoryId(request.categoryId());
        expense.setExpenseDate(request.expenseDate());
        expense.setStatus(ExpenseStatus.DRAFT);

        Expense updated = expenseRepository.save(expense);

        return toResponse(updated);
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
