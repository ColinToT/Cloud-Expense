import { http } from "@/api/http";
import type { Expense, ExpenseDetail } from "@/types/expense";

export const getMyExpenses = async (): Promise<Expense[]> => {
  const response = await http.get<Expense[]>("/expenses");
  return response.data;
};

export const getExpenseById = async (id: number): Promise<ExpenseDetail> => {
  const response = await http.get<ExpenseDetail>(`/expenses/${id}`);
  return response.data;
};

export const submitExpense = async (id: number): Promise<void> => {
  await http.post(`/expenses/${id}/submit`);
};
