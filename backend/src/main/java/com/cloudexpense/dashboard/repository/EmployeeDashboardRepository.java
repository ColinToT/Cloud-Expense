package com.cloudexpense.dashboard.repository;

import com.cloudexpense.dashboard.repository.projection.EmployeeDashboardProjection;
import com.cloudexpense.expense.entity.Expense;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * ClassName: EmployeeDashboardRepository
 * Package: com.cloudexpense.dashboard.repository
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 20:35
 * @Version: v1.0
 */
public interface EmployeeDashboardRepository extends Repository<Expense, Long> {

    @Query(
            value = """
                    select
                    count(e.id) as totalCount,
                    coalesce(sum(e.amount),0) as totalAmount,

                    coalesce(
                        sum(
                            case
                                when e.status = 'DRAFT'
                                then 1
                                else 0
                            end
                        ),
                    0)
                    as draftCount,

                    coalesce(
                        sum(
                            case
                                when e.status in
                                (
                                    'SUBMITTED',
                                    'MANAGER_APPROVED'
                                )
                                then 1
                                else 0
                            end
                        ),
                    0)
                    as pendingApprovalCount,

                    coalesce(
                        sum(
                            case
                                when e.status = 'FINANCE_APPROVED'
                                then 1
                                else 0
                            end
                        ),
                    0)
                    as approvedCount,

                    coalesce(
                        sum(
                            case
                                when e.status = 'PAID'
                                then 1
                                else 0
                            end
                        ),
                    0)
                    as paidCount,

                    coalesce(
                        sum(
                            case
                                when e.status = 'REJECTED'
                                then 1
                                else 0
                            end
                        ),
                    0)
                    as rejectedCount
                    from expenses e
                    where e.user_id = :userId
                    and e.deleted_at is null
                    """,
            nativeQuery = true
    )
    EmployeeDashboardProjection getSummary(@Param("userId") Long userId);
}
