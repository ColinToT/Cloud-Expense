import { http } from "@/api/http";
import type { Expense } from "@/types/expense";

export const getMyExpenses = async (): Promise<Expense[]> => {
  const response = await http.get<Expense[]>("/expenses");
  return response.data;
};
