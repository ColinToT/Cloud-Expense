export type NotificationType =
  | "EXPENSE_SUBMITTED"
  | "EXPENSE_APPROVED"
  | "EXPENSE_REJECTED"
  | "PAYMENT_COMPLETED";

export interface Notification {
  id: number;
  type: NotificationType;
  title: string;
  message: string;
  referenceId: number | null;
  readStatus: boolean;
  createdAt: string;
}
