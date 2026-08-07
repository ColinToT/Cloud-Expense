package com.cloudexpense.dashboard.repository.projection;

import java.math.BigDecimal;

/**
 * ClassName: EmployeeDashboardProjection
 * Package: com.cloudexpense.dashboard.repository
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/5 20:37
 * @Version: v1.0
 */
public interface EmployeeDashboardProjection {

    Long getTotalCount();

    BigDecimal getTotalAmount();

    Long getDraftCount();

    Long getPendingApprovalCount();

    Long getApprovedCount();

    Long getPaidCount();

    Long getRejectedCount();
}
