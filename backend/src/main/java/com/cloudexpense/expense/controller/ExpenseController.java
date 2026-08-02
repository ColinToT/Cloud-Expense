package com.cloudexpense.expense.controller;

import com.cloudexpense.expense.dto.CreateExpenseRequest;
import com.cloudexpense.expense.dto.ExpenseDetailResponse;
import com.cloudexpense.expense.dto.ExpenseResponse;
import com.cloudexpense.expense.dto.UpdateExpenseRequest;
import com.cloudexpense.expense.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@RequestBody CreateExpenseRequest request){
        ExpenseResponse response = expenseService.createExpense(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDetailResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(
                expenseService.getExpense(id)
        );
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(
            @PathVariable Long id,
            @RequestBody UpdateExpenseRequest request
    ){
        return ResponseEntity.ok(
                expenseService.updateExpense(id, request)
        );
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/{id}/submit")
    public ResponseEntity<ExpenseResponse> submit(@PathVariable Long id){
        return ResponseEntity.ok(
                expenseService.submitExpense(id)
        );
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getMyExpenses(){
        return ResponseEntity.ok(
                expenseService.getMyExpenses()
        );
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
