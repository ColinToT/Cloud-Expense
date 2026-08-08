package com.cloudexpense.approval.controller;

import com.cloudexpense.approval.dto.ApprovalHistoryResponse;
import com.cloudexpense.approval.dto.ApprovalRequest;
import com.cloudexpense.approval.dto.PendingApprovalResponse;
import com.cloudexpense.approval.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: ApprovalController
 * Package: com.cloudexpense.approval.controller
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/1 21:13
 * @Version: v1.0
 */
@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {
    private final ApprovalService approvalService;

    @PreAuthorize("hasRole('MANAGER') or hasRole('FINANCE')")
    @GetMapping("/pending")
    public ResponseEntity<List<PendingApprovalResponse>> pending(){
        return ResponseEntity.ok(
                approvalService.getPendingExpenses()
        );
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('FINANCE')")
    @PostMapping("/{expenseId}/approve")
    public ResponseEntity<Void> approve(
            @PathVariable Long expenseId,
            @RequestBody ApprovalRequest request
    ){
        approvalService.approve(expenseId, request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('FINANCE')")
    @PostMapping("/{expenseId}/reject")
    public ResponseEntity<Void> reject(
            @PathVariable Long expenseId,
            @RequestBody ApprovalRequest request
    ){
        approvalService.reject(expenseId, request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','FINANCE')")
    @GetMapping("/{expenseId}/history")
    public ResponseEntity<List<ApprovalHistoryResponse>> history(
            @PathVariable Long expenseId
    ){
        return ResponseEntity.ok(
                approvalService.getApprovalHistory(expenseId)
        );
    }
}
