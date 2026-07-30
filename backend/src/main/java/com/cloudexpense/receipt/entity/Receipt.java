package com.cloudexpense.receipt.entity;

import com.cloudexpense.expense.entity.Expense;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

/**
 * ClassName: Receipt
 * Package: com.cloudexpense.receipt.entity
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/30 21:03
 * @Version: v1.0
 */
@Entity
@Table(name = "receipts")
@Getter
@Setter
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @Column(name="file_name", nullable=false)
    private String fileName;

    @Column(
            name="file_url",
            nullable=false,
            columnDefinition="TEXT"
    )
    private String fileUrl;

    @CreationTimestamp
    @Column(name="uploaded_at")
    private OffsetDateTime uploadedAt;
}
