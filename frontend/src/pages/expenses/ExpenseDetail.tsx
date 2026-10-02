import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { Modal, Button, Spin, message } from "antd";
import { ArrowLeftOutlined } from "@ant-design/icons";
import "./ExpenseDetail.css";
import { deleteExpense, getExpenseById, submitExpense } from "@/api/expense";
import type { ExpenseDetail } from "@/types/expense";
import ExpenseStatusTag from "@/components/ExpenseStatusTag/ExpenseStatusTag";
import ReceiptSection from "./components/ReceiptSection";
import { getExpenseCategoryLabel } from "@/constants/expenseCategories";
import { getApprovalHistory } from "@/api/approval";
import type { ApprovalHistory } from "@/types/approval";

const ExpenseDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [expense, setExpense] = useState<ExpenseDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const isEditable =
    expense?.status === "DRAFT" || expense?.status === "REJECTED";
  const hasReceipts = (expense?.receipts.length ?? 0) > 0;
  const [submitting, setSubmitting] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [approvalHistory, setApprovalHistory] = useState<ApprovalHistory[]>([]);

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

  const loadApprovalHistory = async (expenseId: number) => {
    try {
      const data = await getApprovalHistory(expenseId);
      setApprovalHistory(data);
    } catch {
      message.error("Failed to load approval history.");
    }
  };

  useEffect(() => {
    if (!id) {
      return;
    }
    const expenseId = Number(id);
    loadExpense();
    loadApprovalHistory(expenseId);
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

  const handleDelete = async () => {
    if (!expense) {
      return;
    }

    try {
      setDeleting(true);

      await deleteExpense(expense.id);

      message.success("Expense deleted successfully.");

      navigate("/expenses");
    } catch {
      message.error("Failed to delete expense.");
    } finally {
      setDeleting(false);
    }
  };

  const handleDeleteClick = () => {
    Modal.confirm({
      title: "Delete this expense?",
      content:
        "This action cannot be undone. The expense and its attached receipts will be permanently deleted.",
      okText: "Delete",
      okType: "danger",
      cancelText: "Cancel",
      onOk: handleDelete,
    });
  };

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

        {isEditable && (
          <div className="expense-detail__actions">
            <div className="expense-detail__action-buttons">
              <Button onClick={() => navigate(`/expenses/${expense.id}/edit`)}>
                Edit
              </Button>

              <Button danger loading={deleting} onClick={handleDeleteClick}>
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

      <div className="expense-detail-layout">
        <div className="expense-detail-main">
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
                <strong>{getExpenseCategoryLabel(expense.categoryId)}</strong>
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
            editable={isEditable}
            onReceiptChanged={loadExpense}
          />
        </div>

        <aside className="expense-detail-sidebar">
          {approvalHistory.length > 0 && (
            <section className="expense-detail-card">
              <h2>Approval history</h2>

              <div className="approval-history">
                {approvalHistory.map((record, index) => (
                  <div
                    className="approval-history__item"
                    key={`${record.stage}-${record.createdAt}-${index}`}
                  >
                    <div className="approval-history__header">
                      <div>
                        <strong>
                          {record.stage === "MANAGER"
                            ? "Manager review"
                            : "Finance review"}
                        </strong>

                        <div className="approval-history__meta">
                          {record.action === "APPROVE"
                            ? "Approved"
                            : "Rejected"}{" "}
                          by {record.approverName}
                        </div>
                      </div>

                      <span className="approval-history__date">
                        {new Date(record.createdAt).toLocaleString()}
                      </span>
                    </div>

                    {record.comment && (
                      <div className="approval-history__comment">
                        {record.comment}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </section>
          )}
        </aside>
      </div>
    </div>
  );
};

export default ExpenseDetailPage;
