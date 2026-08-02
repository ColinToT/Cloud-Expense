package com.cloudexpense.approval.repository;

import com.cloudexpense.approval.entity.ApprovalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ClassName: ApprovalRecordRepository
 * Package: com.cloudexpense.approval.repository
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/1 20:52
 * @Version: v1.0
 */
@Repository
public interface ApprovalRecordRepository extends JpaRepository<ApprovalRecord, Long> {
    List<ApprovalRecord> findByExpenseIdOrderByCreatedAtDesc(Long expenseId);
}
