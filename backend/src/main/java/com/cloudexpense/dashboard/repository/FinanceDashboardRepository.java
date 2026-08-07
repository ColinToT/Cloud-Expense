package com.cloudexpense.dashboard.repository;

import com.cloudexpense.dashboard.repository.projection.FinanceDashboardProjection;
import com.cloudexpense.expense.entity.Expense;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * ClassName: FinanceDashboardRepository
 * Package: com.cloudexpense.dashboard.repository
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 20:51
 * @Version: v1.0
 */
public interface FinanceDashboardRepository extends Repository<Expense, Long> {
    @Query(
            value = """
                    select
                    coalesce(
                        sum(
                            case
                                when e.status='MANAGER_APPROVED'
                                then 1
                                else 0
                            end
                        ),
                    0)
                    as pendingApprovalCount,

                    coalesce(
                        sum(
                            case
                                when e.status='MANAGER_APPROVED'
                                then e.amount
                                else 0
                            end
                        ),
                    0)
                    as pendingApprovalAmount,

                    coalesce(
                        sum(
                            case
                                when e.status='FINANCE_APPROVED'
                                then 1
                                else 0
                            end
                        ),
                    0)
                    as pendingPaymentCount,

                    coalesce(
                        sum(
                            case
                                when e.status='FINANCE_APPROVED'
                                then e.amount
                                else 0
                            end
                        ),
                    0)
                    as pendingPaymentAmount,

                    coalesce(
                        sum(
                            case
                                when e.status='PAID'
                                then 1
                                else 0
                            end
                        ),
                    0)
                    as paidCount,

                    coalesce(
                        sum(
                            case
                                when e.status='PAID'
                                then e.amount
                                else 0
                            end
                        ),
                    0)
                    as paidAmount

                    from expenses e
                    where e.deleted_at is null
                    """,
            nativeQuery = true
    )
    FinanceDashboardProjection getSummary();
}
