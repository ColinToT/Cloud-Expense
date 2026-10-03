export interface PendingPayment {
  expenseId: number;
  employeeName: string;
  title: string;
  amount: number;
  currency: string;
  expenseDate: string;
}

export type PaymentMethod = "BANK_TRANSFER" | "CASH" | "OTHER";

export interface PaymentRequest {
  paymentMethod: PaymentMethod;
  transactionReference?: string;
}
