package com.cloudexpense.report.repository;

import com.cloudexpense.expense.entity.Expense;
import com.cloudexpense.report.repository.projection.ExpenseReportProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * ClassName: ReportRepository
 * Package: com.cloudexpense.report.repository
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 21:10
 * @Version: v1.0
 */
public interface ReportRepository extends Repository<Expense, Long> {
    @Query(
            value = """
                    select
                    e.id as expenseId,
                    e.title as title,
                    concat(
                    u.first_name,
                    ' ',
                    u.last_name
                    )
                    as employeeName,
                    e.amount as amount,
                    e.currency as currency,
                    e.status::text as status,
                    e.expense_date as expenseDate
                    from expenses e
                    join users u
                    on u.id=e.user_id
                    where e.deleted_at is null
                    and e.user_id=:userId
                    and (
                    :startDate is null
                    or e.expense_date>=:startDate
                    )
                    and (
                    :endDate is null
                    or e.expense_date<=:endDate
                    )
                    and (
                    :status is null
                    or e.status=cast(:status as expense_status)
                    )
                    order by e.expense_date desc
                    """,
            nativeQuery = true
    )
    List<ExpenseReportProjection> findEmployeeReports(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            String status
    );

    @Query(
            value = """
                    select
                    e.id as expenseId,
                    e.title as title,
                    concat(
                    u.first_name,
                    ' ',
                    u.last_name
                    )
                    as employeeName,
                    e.amount as amount,
                    e.currency as currency,
                    e.status::text as status,
                    e.expense_date as expenseDate
                    from expenses e
                    join users u
                    on u.id=e.user_id
                    where e.deleted_at is null
                    and u.manager_id=:managerId
                    and (
                    :startDate is null
                    or e.expense_date>=:startDate
                    )
                    and (
                    :endDate is null
                    or e.expense_date<=:endDate
                    )
                    and (
                    :status is null
                    or e.status=cast(:status as expense_status)
                    )
                    order by e.expense_date desc
                    """,
            nativeQuery = true
    )
    List<ExpenseReportProjection> findManagerReports(
            Long managerId,
            LocalDate startDate,
            LocalDate endDate,
            String status
    );

    @Query(
            value = """
                    select
                    e.id as expenseId,
                    e.title as title,
                    concat(
                    u.first_name,
                    ' ',
                    u.last_name
                    )
                    as employeeName,
                    e.amount as amount,
                    e.currency as currency,
                    e.status::text as status,
                    e.expense_date as expenseDate
                    from expenses e
                    join users u
                    on u.id=e.user_id
                    where e.deleted_at is null
                    and (
                    :startDate is null
                    or e.expense_date>=:startDate
                    )
                    and (
                    :endDate is null
                    or e.expense_date<=:endDate
                    )
                    and (
                    :status is null
                    or e.status=cast(:status as expense_status)
                    )
                    order by e.expense_date desc
                    """,
            nativeQuery = true
    )
    List<ExpenseReportProjection> findAllReports(
            LocalDate startDate,
            LocalDate endDate,
            String status
    );
}
