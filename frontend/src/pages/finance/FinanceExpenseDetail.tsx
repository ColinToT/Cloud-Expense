import { Button, Input, message, Spin } from "antd";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";

import { getFinanceExpenseDetail } from "@/api/finance";
import { approveExpense, rejectExpense } from "@/api/approval";
import type { FinanceExpenseDetail as FinanceExpenseDetailType } from "@/types/finance";

import ReceiptSection from "@/pages/expenses/components/ReceiptSection";

import "./FinanceExpenseDetail.css";
import ExpenseStatusTag from "@/components/ExpenseStatusTag/ExpenseStatusTag";
import ApprovalHistorySection from "@/components/ApprovalHistory/ApprovalHistorySection";
import { getExpenseCategoryLabel } from "@/constants/expenseCategories";

const { TextArea } = Input;

const FinanceExpenseDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [expense, setExpense] = useState<FinanceExpenseDetailType | null>(null);

  const [comment, setComment] = useState("");
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const loadExpense = async () => {
      if (!id) {
        return;
      }

      try {
        const data = await getFinanceExpenseDetail(Number(id));

        setExpense(data);
      } catch {
        message.error("Failed to load expense.");
      } finally {
        setLoading(false);
      }
    };

    loadExpense();
  }, [id]);

  const handleApprove = async () => {
    if (!expense) {
      return;
    }

    try {
      setSubmitting(true);

      await approveExpense(expense.id, {
        comment: comment.trim(),
      });

      message.success("Expense approved successfully.");

      navigate("/finance/expenses");
    } catch {
      message.error("Failed to approve expense.");
    } finally {
      setSubmitting(false);
    }
  };

  const handleReject = async () => {
    if (!expense) {
      return;
    }

    try {
      setSubmitting(true);

      await rejectExpense(expense.id, {
        comment: comment.trim(),
      });

      message.success("Expense rejected successfully.");

      navigate("/finance/expenses");
    } catch {
      message.error("Failed to reject expense.");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return <Spin />;
  }

  if (!expense) {
    return <div>Expense not found.</div>;
  }

  const canReview = expense.status === "MANAGER_APPROVED";

  return (
    <div className="finance-detail-page">
      <div className="finance-detail-header">
        <div>
          <Button
            type="text"
            className="detail-back-button"
            onClick={() => navigate("/finance/expenses")}
          >
            ← Back to finance review
          </Button>

          <div className="finance-detail-title">
            <h1>{expense.title}</h1>

            <ExpenseStatusTag status={expense.status} />
          </div>
        </div>
      </div>

      <div className="finance-detail-layout">
        <div className="finance-detail-main">
          <section className="finance-detail-card">
            <h2>Expense information</h2>

            <div className="finance-detail-grid">
              <Info label="Employee" value={expense.employeeName} />

              <Info label="Email" value={expense.employeeEmail} />

              <Info
                label="Amount"
                value={`${expense.currency} ${expense.amount.toFixed(2)}`}
              />

              <Info label="Expense date" value={expense.expenseDate} />

              <Info
                label="Category"
                value={getExpenseCategoryLabel(expense.categoryId)}
              />

              <Info
                label="Submitted"
                value={
                  expense.submittedAt
                    ? new Date(expense.submittedAt).toLocaleString()
                    : "-"
                }
              />
            </div>

            <div className="finance-detail-description">
              <span>Description</span>
              <p>{expense.description || "-"}</p>
            </div>
          </section>

          <ReceiptSection expenseId={expense.id} receipts={expense.receipts} />

          <ApprovalHistorySection history={expense.approvalHistory} />
        </div>

        {canReview && (
          <aside className="finance-detail-sidebar">
            <section className="finance-detail-card">
              <h2>Finance decision</h2>

              <TextArea
                rows={5}
                placeholder="Add a comment..."
                value={comment}
                onChange={(event) => setComment(event.target.value)}
              />

              <div className="finance-detail-actions">
                <Button danger disabled={submitting} onClick={handleReject}>
                  Reject
                </Button>

                <Button
                  type="primary"
                  loading={submitting}
                  onClick={handleApprove}
                >
                  Approve
                </Button>
              </div>
            </section>
          </aside>
        )}
      </div>
    </div>
  );
};

interface InfoProps {
  label: string;
  value: string;
}

const Info = ({ label, value }: InfoProps) => (
  <div className="finance-detail-info">
    <span>{label}</span>
    <strong>{value}</strong>
  </div>
);

export default FinanceExpenseDetail;
