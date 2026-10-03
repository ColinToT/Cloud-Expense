import { http } from "@/api/http";
import type { FinanceExpense, FinanceExpenseDetail } from "@/types/finance";

export const getFinanceExpenses = async () => {
  const response = await http.get<FinanceExpense[]>("/finance/expenses");

  return response.data;
};

export const getFinanceExpenseDetail = async (id: number) => {
  const response = await http.get<FinanceExpenseDetail>(
    `/finance/expenses/${id}`,
  );

  return response.data;
};
