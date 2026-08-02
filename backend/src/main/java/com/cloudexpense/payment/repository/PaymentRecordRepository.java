package com.cloudexpense.payment.repository;

import com.cloudexpense.payment.entity.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ClassName: PaymentRecordRepository
 * Package: com.cloudexpense.payment.repository
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 20:51
 * @Version: v1.0
 */
@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {

    boolean existsByExpenseId(Long expenseId);
}