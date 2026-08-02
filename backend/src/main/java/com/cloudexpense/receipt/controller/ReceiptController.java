package com.cloudexpense.receipt.controller;

import com.cloudexpense.receipt.dto.ReceiptResponse;
import com.cloudexpense.receipt.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ClassName: ReceiptController
 * Package: com.cloudexpense.receipt.controller
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/30 21:57
 * @Version: v1.0
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping("/expenses/{expenseId}/receipts")
    public ResponseEntity<ReceiptResponse> upload(
            @PathVariable Long expenseId,
            @RequestParam("file") MultipartFile file
    ){
        ReceiptResponse response = receiptService.upload(expenseId, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expenses/{expenseId}/receipts")
    public ResponseEntity<List<ReceiptResponse>> findByExpense(
            @PathVariable Long expenseId
    ){
        return ResponseEntity.ok(
                receiptService.findByExpenseId(expenseId)
        );
    }

    @DeleteMapping("/receipts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        receiptService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
