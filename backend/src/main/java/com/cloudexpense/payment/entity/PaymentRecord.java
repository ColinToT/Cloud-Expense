package com.cloudexpense.payment.entity;

import com.cloudexpense.expense.entity.Expense;
import com.cloudexpense.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * ClassName: PaymentRecord
 * Package: com.cloudexpense.payment.entity
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/2 20:45
 * @Version: v1.0
 */
@Entity
@Table(name = "payment_records")
@Getter
@Setter
public class PaymentRecord {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="expense_id", nullable=false)
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="paid_by", nullable=false)
    private User paidBy;

    @Column(nullable=false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
            name = "payment_method",
            nullable = false,
            columnDefinition = "payment_method"
    )
    private PaymentMethod paymentMethod;

    private String transactionReference;

    @CreationTimestamp
    @Column(name="paid_at", updatable = false)
    private OffsetDateTime paidAt;

    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private OffsetDateTime createdAt;
}
