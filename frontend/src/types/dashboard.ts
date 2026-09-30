export interface EmployeeDashboardData {
  totalCount: number;
  totalAmount: number;
  draftCount: number;
  pendingApprovalCount: number;
  approvedCount: number;
  paidCount: number;
  rejectedCount: number;
}

export interface ManagerDashboardData {
  pendingApprovalCount: number;
  pendingApprovalAmount: number;
  approvedCount: number;
  rejectedCount: number;
}

export interface FinanceDashboardData {
  pendingApprovalCount: number;
  pendingApprovalAmount: number;
  pendingPaymentCount: number;
  pendingPaymentAmount: number;
  paidCount: number;
  paidAmount: number;
}

export type DashboardResponse =
  | {
      role: "EMPLOYEE";
      data: EmployeeDashboardData;
    }
  | {
      role: "MANAGER";
      data: ManagerDashboardData;
    }
  | {
      role: "FINANCE";
      data: FinanceDashboardData;
    };
