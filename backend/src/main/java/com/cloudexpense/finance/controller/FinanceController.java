package com.cloudexpense.finance.controller;

import com.cloudexpense.finance.dto.FinanceExpenseDetailResponse;
import com.cloudexpense.finance.dto.FinanceExpenseResponse;
import com.cloudexpense.finance.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: FinanceController
 * Package: com.cloudexpense.finance.controller
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 20:13
 * @Version: v1.0
 */
@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/expenses")
    public ResponseEntity<List<FinanceExpenseResponse>> expenses(){
        return ResponseEntity.ok(
                financeService.getExpenses()
        );
    }

    @GetMapping("/expenses/{id}")
    public ResponseEntity<FinanceExpenseDetailResponse> detail(@PathVariable Long id){
        return ResponseEntity.ok(
                financeService.getExpenseDetail(id)
        );
    }
}
