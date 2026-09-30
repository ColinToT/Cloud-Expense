import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { Button, Spin, message } from "antd";
import { ArrowLeftOutlined } from "@ant-design/icons";
import "./ExpenseDetail.css";
import { getExpenseById, submitExpense } from "@/api/expense";
import type { ExpenseDetail } from "@/types/expense";
import ExpenseStatusTag from "@/components/ExpenseStatusTag/ExpenseStatusTag";
import ReceiptSection from "./components/ReceiptSection";

const ExpenseDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [expense, setExpense] = useState<ExpenseDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const isDraft = expense?.status === "DRAFT";
  const hasReceipts = (expense?.receipts.length ?? 0) > 0;
  const [submitting, setSubmitting] = useState(false);

  const loadExpense = async () => {
    try {
      const data = await getExpenseById(Number(id));
      setExpense(data);
    } catch (error) {
      console.error("Failed to load expense", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!id) {
      return;
    }

    loadExpense();
  }, [id]);

  if (loading) {
    return <Spin />;
  }

  if (!expense) {
    return <div>Expense not found.</div>;
  }

  const handleSubmit = async () => {
    if (!expense) {
      return;
    }

    try {
      setSubmitting(true);
      await submitExpense(expense.id);
      const updatedExpense = await getExpenseById(expense.id);
      setExpense(updatedExpense);
      message.success("Expense submitted successfully.");
    } catch (error) {
      console.error("Failed to submit expense", error);
      message.error("Failed to submit expense.");
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = () => {};
  const handleDelete = () => {};

  return (
    <div className="expense-detail-page">
      <Button
        type="text"
        icon={<ArrowLeftOutlined />}
        onClick={() => navigate("/expenses")}
      >
        Back to expenses
      </Button>

      <div className="expense-detail__header">
        <div>
          <div className="expense-detail__title-row">
            <h1>{expense.title}</h1>
            <ExpenseStatusTag status={expense.status} />
          </div>

          <p>Review the details of this expense request.</p>
        </div>

        {isDraft && (
          <div className="expense-detail__actions">
            <div className="expense-detail__action-buttons">
              <Button onClick={handleEdit}>Edit</Button>

              <Button danger onClick={handleDelete}>
                Delete
              </Button>

              <Button
                type="primary"
                disabled={!hasReceipts}
                loading={submitting}
                onClick={handleSubmit}
              >
                Submit
              </Button>
            </div>

            {!hasReceipts && (
              <span className="expense-detail__submit-hint">
                Upload at least one receipt before submitting.
              </span>
            )}
          </div>
        )}
      </div>

      <div className="expense-detail-card">
        <h2>Expense information</h2>

        <div className="expense-detail-grid">
          <div>
            <span>Amount</span>
            <strong>
              {expense.currency} {expense.amount.toFixed(2)}
            </strong>
          </div>

          <div>
            <span>Expense date</span>
            <strong>{expense.expenseDate}</strong>
          </div>

          <div>
            <span>Category</span>
            <strong>{expense.categoryId}</strong>
          </div>

          <div>
            <span>Status</span>
            <strong>{expense.status}</strong>
          </div>
        </div>

        <div className="expense-detail-description">
          <span>Description</span>
          <p>{expense.description || "No description provided."}</p>
        </div>
      </div>

      <ReceiptSection
        expenseId={expense.id}
        receipts={expense.receipts}
        editable={isDraft}
        onReceiptChanged={loadExpense}
      />
    </div>
  );
};

export default ExpenseDetailPage;
