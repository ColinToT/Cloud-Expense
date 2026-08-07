package com.cloudexpense.dashboard.repository.projection;

import java.math.BigDecimal;

/**
 * ClassName: ManagerDashboardProjection
 * Package: com.cloudexpense.dashboard.repository.projection
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/5 21:30
 * @Version: v1.0
 */
public interface ManagerDashboardProjection {

    Long getPendingApprovalCount();

    BigDecimal getPendingApprovalAmount();

    Long getApprovedCount();

    Long getRejectedCount();
}
