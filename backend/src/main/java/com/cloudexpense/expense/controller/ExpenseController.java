package com.cloudexpense.expense.controller;

import com.cloudexpense.expense.dto.CreateExpenseRequest;
import com.cloudexpense.expense.dto.ExpenseResponse;
import com.cloudexpense.expense.dto.UpdateExpenseRequest;
import com.cloudexpense.expense.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: ExpenseController
 * Package: com.cloudexpense.expense.controller
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/28 20:56
 * @Version: v1.0
 */
@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(@RequestBody CreateExpenseRequest request){
        return expenseService.createExpense(request);
    }

    @GetMapping("/{id}")
    public ExpenseResponse getById(@PathVariable Long id){
        return expenseService.getExpense(id);
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(
            @PathVariable Long id,
            @RequestBody UpdateExpenseRequest request
    ){

        return expenseService.updateExpense(id, request);
    }

}
