package com.cloudexpense.dashboard.repository.projection;

import java.math.BigDecimal;

/**
 * ClassName: FinanceDashboardProjection
 * Package: com.cloudexpense.dashboard.repository.projection
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/7 20:50
 * @Version: v1.0
 */
public interface FinanceDashboardProjection {

    Long getPendingApprovalCount();

    BigDecimal getPendingApprovalAmount();

    Long getPendingPaymentCount();

    BigDecimal getPendingPaymentAmount();

    Long getPaidCount();

    BigDecimal getPaidAmount();
}
