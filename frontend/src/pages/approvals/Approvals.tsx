import { useEffect, useState } from "react";
import { Button, Empty, message, Spin, Table } from "antd";
import type { ColumnsType } from "antd/es/table";
import { useNavigate } from "react-router";

import { getPendingApprovals } from "@/api/approval";
import type { PendingApproval } from "@/types/approval";

import "./Approvals.css";

const ApprovalsPage = () => {
  const navigate = useNavigate();

  const [approvals, setApprovals] = useState<PendingApproval[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadApprovals = async () => {
      try {
        const data = await getPendingApprovals();
        setApprovals(data);
      } catch {
        message.error("Failed to load pending approvals.");
      } finally {
        setLoading(false);
      }
    };

    void loadApprovals();
  }, []);

  const columns: ColumnsType<PendingApproval> = [
    {
      title: "Employee",
      dataIndex: "employeeName",
      key: "employeeName",
    },
    {
      title: "Expense",
      dataIndex: "title",
      key: "title",
    },
    {
      title: "Expense date",
      dataIndex: "expenseDate",
      key: "expenseDate",
    },
    {
      title: "Amount",
      key: "amount",
      render: (_, record) => `${record.currency} ${record.amount.toFixed(2)}`,
    },
    {
      title: "Submitted",
      dataIndex: "submittedAt",
      key: "submittedAt",
      render: (value: string) => new Date(value).toLocaleDateString(),
    },
    {
      title: "",
      key: "action",
      align: "right",
      render: (_, record) => (
        <Button
          type="link"
          onClick={() => navigate(`/approvals/${record.expenseId}`)}
        >
          Review
        </Button>
      ),
    },
  ];

  if (loading) {
    return (
      <div className="approvals-page__loading">
        <Spin size="large" />
      </div>
    );
  }

  return (
    <div className="approvals-page">
      <div className="approvals-page__header">
        <div>
          <h1>Pending approvals</h1>

          <p>Review expense requests awaiting your decision.</p>
        </div>
      </div>

      <div className="approvals-card">
        {approvals.length === 0 ? (
          <Empty description="No expenses awaiting approval" />
        ) : (
          <Table
            rowKey="expenseId"
            columns={columns}
            dataSource={approvals}
            pagination={false}
          />
        )}
      </div>
    </div>
  );
};

export default ApprovalsPage;
