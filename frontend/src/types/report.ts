import type { ExpenseStatus } from "@/types/expense";

export interface ExpenseReport {
  expenseId: number;
  title: string;
  employeeName: string;
  amount: number;
  currency: string;
  status: ExpenseStatus;
  expenseDate: string;
}

export interface ExpenseReportQuery {
  startDate?: string;
  endDate?: string;
  status?: ExpenseStatus;
}
