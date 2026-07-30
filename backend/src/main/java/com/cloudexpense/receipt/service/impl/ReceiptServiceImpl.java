package com.cloudexpense.receipt.service.impl;

import com.cloudexpense.common.exception.BusinessException;
import com.cloudexpense.common.storage.FileStorageService;
import com.cloudexpense.config.FileProperties;
import com.cloudexpense.expense.entity.Expense;
import com.cloudexpense.expense.entity.ExpenseStatus;
import com.cloudexpense.expense.repository.ExpenseRepository;
import com.cloudexpense.receipt.dto.ReceiptResponse;
import com.cloudexpense.receipt.entity.Receipt;
import com.cloudexpense.receipt.repository.ReceiptRepository;
import com.cloudexpense.receipt.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ClassName: ReceiptServiceImpl
 * Package: com.cloudexpense.receipt.service.impl
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/30 21:43
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ExpenseRepository expenseRepository;
    private final FileStorageService fileStorageService;
    private final FileProperties fileProperties;

    @Override
    @Transactional
    public ReceiptResponse upload(Long expenseId, MultipartFile file) {
        Expense expense =
                expenseRepository.findById(expenseId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Expense not found"
                                )
                        );

        validateExpenseStatus(expense);

        validateFile(file);

        long count = receiptRepository.countByExpenseId(expenseId);

        if(count >= fileProperties.getMaxCountPerExpense()){
            throw new RuntimeException(
                    "Maximum receipt limit reached"
            );
        }

        String fileUrl = fileStorageService.store(file);

        Receipt receipt = new Receipt();
        receipt.setExpense(expense);
        receipt.setFileName(file.getOriginalFilename());
        receipt.setFileUrl(fileUrl);

        Receipt saved = receiptRepository.save(receipt);

        return toResponse(saved);
    }

    private void validateExpenseStatus(Expense expense) {
        if(expense.getStatus() != ExpenseStatus.DRAFT &&
                expense.getStatus() != ExpenseStatus.REJECTED){

            throw new BusinessException(
                    "Only draft or rejected expense can upload files"
            );
        }
    }

    @Override
    public List<ReceiptResponse> findByExpenseId(Long expenseId) {
        return receiptRepository
                .findAllByExpenseId(expenseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long receiptId) {
        Receipt receipt =
                receiptRepository.findById(receiptId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Receipt not found"
                                )
                        );

        fileStorageService.delete(receipt.getFileUrl());

        receiptRepository.delete(receipt);
    }

    private ReceiptResponse toResponse(Receipt receipt){
        return new ReceiptResponse(
                receipt.getId(),
                receipt.getFileName(),
                receipt.getFileUrl(),
                receipt.getUploadedAt()
        );
    }

    private void validateFile(MultipartFile file){
        if(file.isEmpty()){
            throw new RuntimeException(
                    "File is empty"
            );
        }

        if(file.getSize() > fileProperties.getMaxSize().toBytes()){
            throw new RuntimeException(
                    "File size exceeds limit"
            );
        }

        String contentType = file.getContentType();
        if(contentType == null ||
                !fileProperties.getAllowedTypes().contains(contentType)
        ){
            throw new BusinessException(
                    "Unsupported file type"
            );
        }

    }
}
