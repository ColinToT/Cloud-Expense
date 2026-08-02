package com.cloudexpense.payment.service;

import com.cloudexpense.payment.dto.PaymentRequest;
import com.cloudexpense.payment.dto.PendingPaymentResponse;

import java.util.List;

/**
 * ClassName: PaymentService
 * Package: com.cloudexpense.payment.service
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 20:54
 * @Version: v1.0
 */
public interface PaymentService {

    List<PendingPaymentResponse> getPendingPayments();

    void pay(Long expenseId, PaymentRequest request);

}
