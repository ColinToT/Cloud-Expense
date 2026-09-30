export type ExpenseStatus =
  | "DRAFT"
  | "SUBMITTED"
  | "MANAGER_APPROVED"
  | "FINANCE_APPROVED"
  | "REJECTED"
  | "PAID";

export interface Expense {
  id: number;
  title: string;
  description: string | null;
  amount: number;
  currency: string;
  categoryId: number;
  expenseDate: string;
  status: ExpenseStatus;
}

export interface Receipt {
  id: number;
  fileName: string;
  fileUrl: string;
  uploadedAt: string;
}

export interface ExpenseDetail extends Expense {
  submittedAt: string | null;
  receipts: Receipt[];
}
