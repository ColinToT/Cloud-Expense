package com.cloudexpense.receipt.service.impl;

import com.cloudexpense.common.exception.BusinessException;
import com.cloudexpense.common.exception.ResourceNotFoundException;
import com.cloudexpense.common.storage.FileStorageService;
import com.cloudexpense.config.FileProperties;
import com.cloudexpense.expense.entity.Expense;
import com.cloudexpense.expense.entity.ExpenseStatus;
import com.cloudexpense.expense.repository.ExpenseRepository;
import com.cloudexpense.receipt.dto.ReceiptFileResponse;
import com.cloudexpense.receipt.dto.ReceiptResponse;
import com.cloudexpense.receipt.entity.Receipt;
import com.cloudexpense.receipt.repository.ReceiptRepository;
import com.cloudexpense.receipt.service.ReceiptService;
import com.cloudexpense.user.entity.Role;
import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
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
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public ReceiptResponse upload(Long expenseId, MultipartFile file) {

        User currentUser = currentUserService.getCurrentUser();

        Expense expense =
                expenseRepository.findByIdAndDeletedAtIsNull(expenseId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Expense not found"
                                )
                        );

        validateOwner(expense, currentUser);

        validateExpenseStatus(expense);

        validateFile(file);

        long count =
                receiptRepository.countByExpenseId(expenseId);

        if (count >= fileProperties.getMaxCountPerExpense()) {
            throw new BusinessException(
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

    private void validateOwner(
            Expense expense,
            User currentUser
    ) {
        if (currentUser.getRole() != Role.EMPLOYEE ||
                !expense.getUser().getId().equals(currentUser.getId())) {

            throw new BusinessException(
                    "You cannot modify receipts for this expense"
            );
        }
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

        User currentUser = currentUserService.getCurrentUser();

        Expense expense =
                expenseRepository
                        .findByIdAndDeletedAtIsNull(expenseId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Expense not found"
                                )
                        );

        validateViewPermission(expense, currentUser);

        return receiptRepository
                .findAllByExpenseId(expenseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {

        User currentUser = currentUserService.getCurrentUser();

        Receipt receipt =
                receiptRepository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Receipt not found"
                                )
                        );

        Expense expense = receipt.getExpense();

        if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException(
                    "You cannot delete this receipt"
            );
        }

        if (expense.getStatus() != ExpenseStatus.DRAFT &&
                expense.getStatus() != ExpenseStatus.REJECTED) {

            throw new BusinessException(
                    "Receipts can only be deleted from draft or rejected expenses"
            );
        }

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

    @Override
    public ReceiptFileResponse getReceiptFile(Long receiptId) {

        User currentUser = currentUserService.getCurrentUser();

        Receipt receipt =
                receiptRepository.findById(receiptId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Receipt not found"
                                )
                        );

        Expense expense = receipt.getExpense();

        validateViewPermission(expense, currentUser);

        Resource resource =
                fileStorageService.load(receipt.getFileUrl());

        String contentType = getContentType(resource);

        return new ReceiptFileResponse(
                resource,
                contentType,
                receipt.getFileName()
        );
    }

    private void validateViewPermission(
            Expense expense,
            User currentUser
    ) {
        Role role = currentUser.getRole();

        if (role == Role.EMPLOYEE) {

            if (!expense.getUser().getId().equals(currentUser.getId())) {
                throw new BusinessException(
                        "You cannot access this receipt"
                );
            }

            return;
        }

        if (role == Role.MANAGER) {

            Long managerId = expense.getUser().getManagerId();

            if (managerId == null ||
                    !managerId.equals(currentUser.getId())) {

                throw new BusinessException(
                        "You cannot access this receipt"
                );
            }

            return;
        }

        if (role == Role.FINANCE) {
            return;
        }

        throw new BusinessException(
                "You cannot access this receipt"
        );
    }

    private String getContentType(Resource resource) {
        try {
            String contentType =
                    Files.probeContentType(
                            resource.getFile().toPath()
                    );

            return contentType != null
                    ? contentType
                    : "application/octet-stream";

        } catch (IOException e) {
            return "application/octet-stream";
        }
    }
}
