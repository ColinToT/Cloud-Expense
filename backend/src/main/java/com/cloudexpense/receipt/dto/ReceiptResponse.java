package com.cloudexpense.receipt.dto;

import java.time.OffsetDateTime;

/**
 * ClassName: ReceiptResponse
 * Package: com.cloudexpense.receipt.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/30 21:41
 * @Version: v1.0
 */
public record ReceiptResponse(
        Long id,

        String fileName,

        String fileUrl,

        OffsetDateTime uploadedAt
) {
}
