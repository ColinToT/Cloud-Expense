import { http } from "@/api/http";

import type { PaymentRequest, PendingPayment } from "@/types/payment";

export const getPendingPayments = async () => {
  const response = await http.get<PendingPayment[]>("/payments/pending");

  return response.data;
};

export const payExpense = async (
  expenseId: number,
  request: PaymentRequest,
) => {
  await http.post(`/payments/${expenseId}/pay`, request);
};
