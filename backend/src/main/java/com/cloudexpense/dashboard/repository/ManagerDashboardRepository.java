package com.cloudexpense.dashboard.repository;

import com.cloudexpense.dashboard.repository.projection.ManagerDashboardProjection;
import com.cloudexpense.expense.entity.Expense;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * ClassName: ManagerDashboardRepository
 * Package: com.cloudexpense.dashboard.repository
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 20:41
 * @Version: v1.0
 */
public interface ManagerDashboardRepository extends Repository<Expense, Long> {
    @Query(
            value = """
                    select
                    coalesce(
                        sum(
                            case
                                when e.status='SUBMITTED'
                                then 1
                                else 0
                            end
                        ),
                    0)
                    as pendingApprovalCount,

                    coalesce(
                        sum(
                            case
                                when e.status='SUBMITTED'
                                then e.amount
                                else 0
                            end
                        ),
                    0)
                    as pendingApprovalAmount,

                    coalesce(
                        sum(
                            case
                                when e.status='MANAGER_APPROVED'
                                then 1
                                else 0
                            end
                        ),
                    0)
                    as approvedCount,

                    coalesce(
                        sum(
                            case
                                when e.status='REJECTED'
                                then 1
                                else 0
                            end
                        ),
                    0)
                    as rejectedCount
                    from expenses e
                    join users u
                    on u.id=e.user_id
                    where u.manager_id=:managerId
                    and e.deleted_at is null
                    """,
            nativeQuery = true
    )
    ManagerDashboardProjection getSummary(@Param("managerId") Long managerId);
}
