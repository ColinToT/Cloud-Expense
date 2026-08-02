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
    public ResponseEntity<ExpenseResponse> create(@RequestBody CreateExpenseRequest request){
        ExpenseResponse response = expenseService.createExpense(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDetailResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(
                expenseService.getExpense(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(
            @PathVariable Long id,
            @RequestBody UpdateExpenseRequest request
    ){
        return ResponseEntity.ok(
                expenseService.updateExpense(id, request)
        );
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ExpenseResponse> submit(@PathVariable Long id){
        return ResponseEntity.ok(
                expenseService.submitExpense(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getMyExpenses(){
        return ResponseEntity.ok(
                expenseService.getMyExpenses()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
