package com.cloudexpense.notification.listener;

import com.cloudexpense.approval.entity.ApprovalStage;
import com.cloudexpense.common.event.ExpenseApprovedEvent;
import com.cloudexpense.common.event.ExpenseRejectedEvent;
import com.cloudexpense.common.event.ExpenseSubmittedEvent;
import com.cloudexpense.common.event.PaymentCompletedEvent;
import com.cloudexpense.notification.entity.NotificationType;
import com.cloudexpense.notification.service.NotificationService;
import com.cloudexpense.user.entity.Role;
import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * ClassName: NotificationEventListener
 * Package: com.cloudexpense.notification.listener
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/4 20:22
 * @Version: v1.0
 */
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ExpenseApprovedEvent event){
        notificationService.create(
                event.employeeId(),
                NotificationType.EXPENSE_APPROVED,
                "Expense Approved",
                "Your expense has been approved",
                event.expenseId()
        );

        // Manager approved -> notify Finance
        if (event.stage() == ApprovalStage.MANAGER) {

            List<User> financeUsers =
                    userRepository.findByRole(Role.FINANCE);

            for (User finance : financeUsers) {
                notificationService.create(
                        finance.getId(),
                        NotificationType.EXPENSE_APPROVED,
                        "Expense Ready for Finance Review",
                        "An expense requires your review",
                        event.expenseId()
                );
            }
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ExpenseSubmittedEvent event){
        notificationService.create(
                event.managerId(),
                NotificationType.EXPENSE_SUBMITTED,
                "New Expense Submitted",
                "A new expense requires your approval",
                event.expenseId()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ExpenseRejectedEvent event){
        notificationService.create(
                event.employeeId(),
                NotificationType.EXPENSE_REJECTED,
                "Expense Rejected",
                "Your expense has been rejected",
                event.expenseId()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentCompletedEvent event){
        notificationService.create(
                event.employeeId(),
                NotificationType.PAYMENT_COMPLETED,
                "Payment Completed",
                "Your expense payment of "
                        + event.amount()
                        + " has been completed",
                event.expenseId()
        );
    }
}
