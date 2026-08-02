package com.cloudexpense.payment.dto;

import com.cloudexpense.payment.entity.PaymentMethod;

/**
 * ClassName: PaymentRequest
 * Package: com.cloudexpense.payment.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 20:53
 * @Version: v1.0
 */
public record PaymentRequest(
        PaymentMethod paymentMethod,

        String transactionReference
) {
}
