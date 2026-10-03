import { useEffect, useState } from "react";
import { Button, Table } from "antd";
import type { ColumnsType } from "antd/es/table";
import { useNavigate } from "react-router";

import { useAuth } from "@/auth/AuthContext";
import { getPendingApprovals } from "@/api/approval";
import type { ManagerDashboardData } from "@/types/dashboard";
import type { PendingApproval } from "@/types/approval";

import "./Dashboard.css";

interface Props {
  data: ManagerDashboardData;
}

const ManagerDashboard = ({ data }: Props) => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [approvals, setApprovals] = useState<PendingApproval[]>([]);
  const [isLoadingApprovals, setIsLoadingApprovals] = useState(true);

  useEffect(() => {
    const loadApprovals = async () => {
      try {
        const result = await getPendingApprovals();
        setApprovals(result);
      } catch (error) {
        console.error("Failed to load pending approvals", error);
      } finally {
        setIsLoadingApprovals(false);
      }
    };

    void loadApprovals();
  }, []);

  const recentApprovals = approvals.slice(0, 3);

  const statistics = [
    {
      title: "Pending approvals",
      value: data.pendingApprovalCount,
      description: "Expenses waiting for your review",
    },
    {
      title: "Pending amount",
      value: `€${data.pendingApprovalAmount.toFixed(2)}`,
      description: "Total amount awaiting review",
    },
    {
      title: "Approved",
      value: data.approvedCount,
      description: "Expenses you have approved",
    },
    {
      title: "Rejected",
      value: data.rejectedCount,
      description: "Expenses you have rejected",
    },
  ];

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

  return (
    <div className="dashboard-page">
      <div className="dashboard-page__header">
        <div>
          <h1>Good morning, {user?.firstName}</h1>
          <p>Review and manage your team's expense requests.</p>
        </div>

        <Button type="primary" onClick={() => navigate("/approvals")}>
          Review approvals
        </Button>
      </div>

      <div className="dashboard-stats">
        {statistics.map((stat) => (
          <div className="dashboard-stat-card" key={stat.title}>
            <div className="dashboard-stat-card__title">{stat.title}</div>

            <div className="dashboard-stat-card__value">{stat.value}</div>

            <div className="dashboard-stat-card__description">
              {stat.description}
            </div>
          </div>
        ))}
      </div>

      <section className="dashboard-panel">
        <div className="dashboard-panel__header">
          <h2>Pending approvals</h2>

          <Button type="primary" onClick={() => navigate("/approvals")}>
            View all
          </Button>
        </div>

        <Table
          rowKey="expenseId"
          columns={columns}
          dataSource={recentApprovals}
          loading={isLoadingApprovals}
          pagination={false}
          size="middle"
        />
      </section>
    </div>
  );
};

export default ManagerDashboard;
