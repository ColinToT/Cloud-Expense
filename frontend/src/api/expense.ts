import { http } from "@/api/http";
import type {
  Expense,
  ExpenseDetail,
  CreateExpenseRequest,
  UpdateExpenseRequest,
} from "@/types/expense";

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

export const createExpense = async (
  request: CreateExpenseRequest,
): Promise<Expense> => {
  const response = await http.post<Expense>("/expenses", request);

  return response.data;
};

export const updateExpense = async (
  expenseId: number,
  request: UpdateExpenseRequest,
): Promise<Expense> => {
  const response = await http.put<Expense>(`/expenses/${expenseId}`, request);

  return response.data;
};

export const deleteExpense = async (id: number): Promise<void> => {
  await http.delete(`/expenses/${id}`);
};
