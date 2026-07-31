package com.cloudexpense.receipt.repository;

import com.cloudexpense.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ClassName: ReceiptRepository
 * Package: com.cloudexpense.receipt.repository
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/30 21:06
 * @Version: v1.0
 */
@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> findAllByExpenseId(Long expenseId);

    int countByExpenseId(Long expenseId);

    void deleteByExpenseId(Long id);
}
