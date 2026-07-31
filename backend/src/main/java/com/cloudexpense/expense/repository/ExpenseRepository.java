package com.cloudexpense.expense.repository;

import com.cloudexpense.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * ClassName: ExpenseRepository
 * Package: com.cloudexpense.expense.repository
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/28 20:39
 * @Version: v1.0
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByIdAndUserIdAndDeletedAtIsNull(Long id, Long userId);

    List<Expense> findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId);
}
