import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { Button, Input, Modal, Spin, message } from "antd";
import { ArrowLeftOutlined } from "@ant-design/icons";
import {
  approveExpense,
  getApprovalExpenseDetail,
  rejectExpense,
} from "@/api/approval";
import type { ApprovalExpenseDetail } from "@/types/approval";
import ExpenseStatusTag from "@/components/ExpenseStatusTag/ExpenseStatusTag";

import "./ApprovalDetail.css";
import { getExpenseCategoryLabel } from "@/constants/expenseCategories";
import ReceiptSection from "@/pages/expenses/components/ReceiptSection";

const ApprovalDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [expense, setExpense] = useState<ApprovalExpenseDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [comment, setComment] = useState("");
  const [approving, setApproving] = useState(false);
  const [rejecting, setRejecting] = useState(false);

  useEffect(() => {
    if (!id) {
      return;
    }

    const loadExpense = async () => {
      try {
        const data = await getApprovalExpenseDetail(Number(id));
        setExpense(data);
      } catch {
        message.error("Failed to load expense.");
      } finally {
        setLoading(false);
      }
    };

    void loadExpense();
  }, [id]);

  if (loading) {
    return <Spin />;
  }

  if (!expense) {
    return <div>Expense not found.</div>;
  }

  const handleApprove = async () => {
    if (!expense) {
      return;
    }

    try {
      setApproving(true);

      await approveExpense(expense.id, {
        comment: comment.trim(),
      });

      message.success("Expense approved successfully.");
      navigate("/approvals");
    } catch {
      message.error("Failed to approve expense.");
    } finally {
      setApproving(false);
    }
  };

  const handleReject = () => {
    if (!expense) {
      return;
    }

    if (!comment.trim()) {
      message.warning("Please provide a reason for rejecting this expense.");
      return;
    }

    Modal.confirm({
      title: "Reject expense?",
      content: "This expense will be returned as rejected.",
      okText: "Reject",
      okButtonProps: {
        danger: true,
      },
      cancelText: "Cancel",
      onOk: async () => {
        try {
          setRejecting(true);

          await rejectExpense(expense.id, {
            comment: comment.trim(),
          });

          message.success("Expense rejected successfully.");
          navigate("/approvals");
        } catch {
          message.error("Failed to reject expense.");
        } finally {
          setRejecting(false);
        }
      },
    });
  };

  return (
    <div className="approval-detail-page">
      <Button
        type="text"
        icon={<ArrowLeftOutlined />}
        onClick={() => navigate("/approvals")}
      >
        Back to approvals
      </Button>

      <div className="approval-detail__header">
        <div>
          <div className="approval-detail__title-row">
            <h1>{expense.title}</h1>
            <ExpenseStatusTag status={expense.status} />
          </div>

          <p>Review this expense request and make your decision.</p>
        </div>
      </div>

      <div className="approval-detail-layout">
        <main className="approval-detail-main">
          <section className="approval-detail-card">
            <h2>Expense information</h2>

            <div className="approval-detail-grid">
              <div>
                <span>Employee</span>
                <strong>{expense.employeeName}</strong>
              </div>

              <div>
                <span>Email</span>
                <strong>{expense.employeeEmail}</strong>
              </div>

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
                <span>Submitted</span>
                <strong>
                  {new Date(expense.submittedAt).toLocaleDateString()}
                </strong>
              </div>
            </div>

            <div className="approval-detail-description">
              <span>Description</span>
              <p>{expense.description || "No description provided."}</p>
            </div>
          </section>

          <ReceiptSection expenseId={expense.id} receipts={expense.receipts} />
        </main>

        <aside className="approval-detail-sidebar">
          <section className="approval-detail-card">
            <h2>Decision</h2>

            <div className="approval-decision">
              <div className="approval-decision__field">
                <label>Comment</label>

                <Input.TextArea
                  value={comment}
                  onChange={(e) => setComment(e.target.value)}
                  placeholder="Add a comment about your decision"
                  rows={4}
                  maxLength={500}
                  showCount
                />
              </div>

              <div className="approval-decision__actions">
                <Button danger loading={rejecting} onClick={handleReject}>
                  Reject
                </Button>

                <Button
                  type="primary"
                  loading={approving}
                  onClick={handleApprove}
                >
                  Approve
                </Button>
              </div>
            </div>
          </section>
        </aside>
      </div>
    </div>
  );
};

export default ApprovalDetailPage;
