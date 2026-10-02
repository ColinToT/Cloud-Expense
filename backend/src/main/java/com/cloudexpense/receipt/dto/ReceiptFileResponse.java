package com.cloudexpense.receipt.dto;

import org.springframework.core.io.Resource;

/**
 * ClassName: ReceiptFileResponse
 * Package: com.cloudexpense.receipt.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/10/2 21:30
 * @Version: v1.0
 */
public record ReceiptFileResponse(
        Resource resource,
        String contentType,
        String fileName
) {
}
