package com.cloudexpense.expense.service;

import com.cloudexpense.expense.dto.CreateExpenseRequest;
import com.cloudexpense.expense.dto.ExpenseResponse;
import com.cloudexpense.expense.dto.UpdateExpenseRequest;

import java.util.List;

/**
 * ClassName: ExpenseService
 * Package: com.cloudexpense.expense.service
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/28 20:49
 * @Version: v1.0
 */
public interface ExpenseService {

    ExpenseResponse createExpense(CreateExpenseRequest request);

    ExpenseResponse getExpense(Long id);

    ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request);

    ExpenseResponse submitExpense(Long id);

    List<ExpenseResponse> getMyExpenses();

    void delete(Long id);
}
