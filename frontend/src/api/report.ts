import { http } from "@/api/http";
import type { ExpenseReport, ExpenseReportQuery } from "@/types/report";

export const getExpenseReports = async (
  query: ExpenseReportQuery = {},
): Promise<ExpenseReport[]> => {
  const response = await http.get<ExpenseReport[]>("/reports/expenses", {
    params: query,
  });

  return response.data;
};

export const exportExpenseReports = async (
  query: ExpenseReportQuery = {},
): Promise<Blob> => {
  const response = await http.get("/reports/expenses/export", {
    params: query,
    responseType: "blob",
  });

  return response.data;
};
