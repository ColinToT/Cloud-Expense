package com.cloudexpense.payment.service.impl;

import com.cloudexpense.common.exception.BusinessException;
import com.cloudexpense.expense.entity.Expense;
import com.cloudexpense.expense.entity.ExpenseStatus;
import com.cloudexpense.expense.repository.ExpenseRepository;
import com.cloudexpense.payment.dto.PaymentRequest;
import com.cloudexpense.payment.dto.PendingPaymentResponse;
import com.cloudexpense.payment.entity.PaymentRecord;
import com.cloudexpense.payment.repository.PaymentRecordRepository;
import com.cloudexpense.payment.service.PaymentService;
import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: PaymentServiceImpl
 * Package: com.cloudexpense.payment.service.impl
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 20:57
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ExpenseRepository expenseRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final CurrentUserService currentUserService;

    @Override
    public List<PendingPaymentResponse> getPendingPayments() {
        return expenseRepository
                .findByStatusAndDeletedAtIsNull(ExpenseStatus.FINANCE_APPROVED)
                .stream()
                .map(this::toPendingResponse)
                .toList();
    }

    private PendingPaymentResponse toPendingResponse(Expense expense){
        User employee = expense.getUser();

        return new PendingPaymentResponse(
                expense.getId(),
                employee.getFirstName()
                        +
                        " "
                        +
                        employee.getLastName(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getCurrency(),
                expense.getExpenseDate()
        );
    }

    @Override
    public void pay(Long expenseId, PaymentRequest request) {
        User finance = currentUserService.getCurrentUser();

        Expense expense =
                expenseRepository
                        .findByIdAndDeletedAtIsNull(
                                expenseId
                        )
                        .orElseThrow(
                                () -> new BusinessException(
                                        "Expense not found"
                                )
                        );

        if(expense.getStatus() != ExpenseStatus.FINANCE_APPROVED){
            throw new BusinessException(
                    "Expense is not ready for payment"
            );
        }

        if(paymentRecordRepository.existsByExpenseId(expenseId)){
            throw new BusinessException(
                    "Expense already paid"
            );
        }

        PaymentRecord record = new PaymentRecord();
        record.setExpense(expense);
        record.setPaidBy(finance);
        record.setAmount(expense.getAmount());
        record.setPaymentMethod(request.paymentMethod());
        record.setTransactionReference(request.transactionReference());
        paymentRecordRepository.save(record);

        expense.setStatus(ExpenseStatus.PAID);
        expenseRepository.save(expense);
    }
}
