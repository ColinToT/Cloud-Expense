import { http } from "@/api/http";
import type {
  ApprovalExpenseDetail,
  ApprovalHistory,
  ApprovalRequest,
  PendingApproval,
} from "@/types/approval";

export const getPendingApprovals = async (): Promise<PendingApproval[]> => {
  const response = await http.get<PendingApproval[]>("/approvals/pending");

  return response.data;
};

export const approveExpense = async (
  expenseId: number,
  request: ApprovalRequest,
): Promise<void> => {
  await http.post(`/approvals/${expenseId}/approve`, request);
};

export const rejectExpense = async (
  expenseId: number,
  request: ApprovalRequest,
): Promise<void> => {
  await http.post(`/approvals/${expenseId}/reject`, request);
};

export const getApprovalHistory = async (
  expenseId: number,
): Promise<ApprovalHistory[]> => {
  const response = await http.get<ApprovalHistory[]>(
    `/approvals/${expenseId}/history`,
  );

  return response.data;
};

export const getApprovalExpenseDetail = async (
  expenseId: number,
): Promise<ApprovalExpenseDetail> => {
  const response = await http.get<ApprovalExpenseDetail>(
    `/approvals/${expenseId}`,
  );

  return response.data;
};
