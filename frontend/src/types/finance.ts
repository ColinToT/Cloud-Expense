import type { ExpenseStatus, Receipt } from "@/types/expense";
import type { ApprovalHistory } from "@/types/approval";

export interface FinanceExpense {
  id: number;
  employeeName: string;
  title: string;
  amount: number;
  currency: string;
  status: ExpenseStatus;
  expenseDate: string;
}

export interface FinanceExpenseDetail {
  id: number;
  employeeName: string;
  employeeEmail: string;
  title: string;
  description: string;
  amount: number;
  currency: string;
  categoryId: number;
  expenseDate: string;
  status: ExpenseStatus;
  submittedAt: string;
  receipts: Receipt[];
  approvalHistory: ApprovalHistory[];
}
