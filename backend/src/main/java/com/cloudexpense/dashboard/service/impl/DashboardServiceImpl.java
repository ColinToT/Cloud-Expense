package com.cloudexpense.dashboard.service.impl;

import com.cloudexpense.dashboard.dto.DashboardResponse;
import com.cloudexpense.dashboard.dto.EmployeeDashboardResponse;
import com.cloudexpense.dashboard.dto.FinanceDashboardResponse;
import com.cloudexpense.dashboard.dto.ManagerDashboardResponse;
import com.cloudexpense.dashboard.repository.EmployeeDashboardRepository;
import com.cloudexpense.dashboard.repository.FinanceDashboardRepository;
import com.cloudexpense.dashboard.repository.ManagerDashboardRepository;
import com.cloudexpense.dashboard.repository.projection.EmployeeDashboardProjection;
import com.cloudexpense.dashboard.repository.projection.FinanceDashboardProjection;
import com.cloudexpense.dashboard.repository.projection.ManagerDashboardProjection;
import com.cloudexpense.dashboard.service.DashboardService;
import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ClassName: DashboardServiceImpl
 * Package: com.cloudexpense.dashboard.service.impl
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/5 20:58
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CurrentUserService currentUserService;
    private final EmployeeDashboardRepository employeeDashboardRepository;
    private final ManagerDashboardRepository managerDashboardRepository;
    private final FinanceDashboardRepository financeDashboardRepository;

    @Override
    public DashboardResponse getDashboard() {
        User user = currentUserService.getCurrentUser();

        return switch (user.getRole()) {

            case EMPLOYEE -> getEmployeeDashboard(user);

            case MANAGER -> getManagerDashboard(user);

            case FINANCE -> getFinanceDashboard();

            case ADMIN -> throw new UnsupportedOperationException();

        };
    }

    private DashboardResponse getEmployeeDashboard(User user) {
        EmployeeDashboardProjection result =
                employeeDashboardRepository.getSummary(user.getId());

        EmployeeDashboardResponse data =
                new EmployeeDashboardResponse(
                        result.getTotalCount(),
                        result.getTotalAmount(),
                        result.getDraftCount(),
                        result.getPendingApprovalCount(),
                        result.getApprovedCount(),
                        result.getPaidCount(),
                        result.getRejectedCount()
                );

        return new DashboardResponse("EMPLOYEE", data);
    }

    private DashboardResponse getManagerDashboard(User user) {
        ManagerDashboardProjection result =
                managerDashboardRepository.getSummary(user.getId());

        ManagerDashboardResponse data =
                new ManagerDashboardResponse(
                        result.getPendingApprovalCount(),
                        result.getPendingApprovalAmount(),
                        result.getApprovedCount(),
                        result.getRejectedCount()
                );

        return new DashboardResponse("MANAGER", data);
    }

    private DashboardResponse getFinanceDashboard() {
        FinanceDashboardProjection result =
                financeDashboardRepository.getSummary();

        FinanceDashboardResponse data =
                new FinanceDashboardResponse(
                        result.getPendingApprovalCount(),
                        result.getPendingApprovalAmount(),
                        result.getPendingPaymentCount(),
                        result.getPendingPaymentAmount(),
                        result.getPaidCount(),
                        result.getPaidAmount()
                );

        return new DashboardResponse("FINANCE", data);
    }
}
