import type { ExpenseStatus } from "@/types/expense";
import "./ExpenseStatusTag.css";

interface Props {
  status: ExpenseStatus;
}

const statusLabels: Record<ExpenseStatus, string> = {
  DRAFT: "DRAFT",
  SUBMITTED: "SUBMITTED",
  MANAGER_APPROVED: "MANAGER APPROVED",
  FINANCE_APPROVED: "FINANCE APPROVED",
  REJECTED: "REJECTED",
  PAID: "PAID",
};

const ExpenseStatusTag = ({ status }: Props) => {
  return (
    <span className={`expense-status expense-status--${status.toLowerCase()}`}>
      {statusLabels[status]}
    </span>
  );
};

export default ExpenseStatusTag;
