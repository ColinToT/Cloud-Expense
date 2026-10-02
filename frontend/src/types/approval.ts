import type { ExpenseStatus } from "./expense";

export interface PendingApproval {
  expenseId: number;
  title: string;
  amount: number;
  currency: string;
  expenseDate: string;
  employeeName: string;
  submittedAt: string;
}

export interface ApprovalRequest {
  comment?: string;
}

export interface ApprovalHistory {
  stage: string;
  action: string;
  approverName: string;
  comment: string | null;
  createdAt: string;
}

export interface ApprovalExpenseDetail {
  id: number;
  employeeName: string;
  employeeEmail: string;
  title: string;
  description: string | null;
  amount: number;
  currency: string;
  categoryId: number;
  expenseDate: string;
  status: ExpenseStatus;
  submittedAt: string;
  receipts: ReceiptResponse[];
}

export interface ReceiptResponse {
  id: number;
  fileName: string;
  fileUrl: string;
  uploadedAt: string;
}
