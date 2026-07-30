package com.cloudexpense.receipt.service;

import com.cloudexpense.receipt.dto.ReceiptResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ClassName: ReceiptService
 * Package: com.cloudexpense.receipt.service
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/30 21:42
 * @Version: v1.0
 */
public interface ReceiptService {
    ReceiptResponse upload(Long expenseId, MultipartFile file);

    List<ReceiptResponse> findByExpenseId(Long expenseId);

    void delete(Long receiptId);
}
