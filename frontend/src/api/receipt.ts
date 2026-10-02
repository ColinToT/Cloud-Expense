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

export const getReceiptFile = async (receiptId: number) => {
  const response = await http.get(`/receipts/${receiptId}/file`, {
    responseType: "blob",
  });

  return response.data;
};

export const deleteReceipt = async (receiptId: number) => {
  await http.delete(`/receipts/${receiptId}`);
};
