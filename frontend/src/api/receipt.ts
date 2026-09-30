import { http } from "./http";
import type { Receipt } from "@/types/expense";

export const uploadReceipt = async (
  expenseId: number,
  file: File,
): Promise<Receipt> => {
  const formData = new FormData();

  formData.append("file", file);

  const response = await http.post<Receipt>(
    `/expenses/${expenseId}/receipts`,
    formData,
  );

  return response.data;
};
