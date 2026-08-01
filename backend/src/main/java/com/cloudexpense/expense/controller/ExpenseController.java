package com.cloudexpense.expense.controller;

import com.cloudexpense.expense.dto.CreateExpenseRequest;
import com.cloudexpense.expense.dto.ExpenseDetailResponse;
import com.cloudexpense.expense.dto.ExpenseResponse;
import com.cloudexpense.expense.dto.UpdateExpenseRequest;
import com.cloudexpense.expense.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ExpenseDetailResponse getById(@PathVariable Long id){
        return expenseService.getExpense(id);
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(
            @PathVariable Long id,
            @RequestBody UpdateExpenseRequest request
    ){
        return expenseService.updateExpense(id, request);
    }

    @PostMapping("/{id}/submit")
    public ExpenseResponse submit(@PathVariable Long id){
        return expenseService.submitExpense(id);
    }

    @GetMapping
    public List<ExpenseResponse> getMyExpenses(){
        return expenseService.getMyExpenses();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
