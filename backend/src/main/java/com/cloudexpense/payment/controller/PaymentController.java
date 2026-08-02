package com.cloudexpense.payment.controller;

import com.cloudexpense.payment.dto.PaymentRequest;
import com.cloudexpense.payment.dto.PendingPaymentResponse;
import com.cloudexpense.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: PaymentController
 * Package: com.cloudexpense.payment.controller
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 21:00
 * @Version: v1.0
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/pending")
    public ResponseEntity<List<PendingPaymentResponse>> pending(){
        return ResponseEntity.ok(
                paymentService.getPendingPayments()
        );
    }

    @PostMapping("/{expenseId}/pay")
    public ResponseEntity<Void> pay(
            @PathVariable Long expenseId,
            @RequestBody PaymentRequest request
    ){
        paymentService.pay(expenseId, request);
        return ResponseEntity.ok().build();
    }
}
