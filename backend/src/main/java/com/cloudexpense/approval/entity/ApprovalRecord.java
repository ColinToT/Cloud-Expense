package com.cloudexpense.approval.entity;

import com.cloudexpense.expense.entity.Expense;
import com.cloudexpense.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

/**
 * ClassName: ApprovalRecord
 * Package: com.cloudexpense.approval.entity
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/1 20:42
 * @Version: v1.0
 */
@Entity
@Table(name = "approval_records")
@Getter
@Setter
public class ApprovalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "approval_stage",
            nullable = false,
            columnDefinition = "approval_stage"
    )
    private ApprovalStage approvalStage;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "action",
            nullable = false,
            columnDefinition = "approval_action"
    )
    private ApprovalAction action;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

}
