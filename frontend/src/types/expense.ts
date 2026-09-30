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
