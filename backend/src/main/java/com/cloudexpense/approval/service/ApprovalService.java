package com.cloudexpense.approval.service;

import com.cloudexpense.approval.dto.ApprovalHistoryResponse;
import com.cloudexpense.approval.dto.ApprovalRequest;
import com.cloudexpense.approval.dto.PendingApprovalResponse;

import java.util.List;

/**
 * ClassName: ApprovalService
 * Package: com.cloudexpense.approval.service
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/1 21:01
 * @Version: v1.0
 */
public interface ApprovalService {
    List<PendingApprovalResponse> getPendingExpenses();

    void approve(Long expenseId, ApprovalRequest request);

    void reject(Long expenseId, ApprovalRequest request);

    List<ApprovalHistoryResponse> getApprovalHistory(Long expenseId);
}
