import { useAuth } from "@/auth/AuthContext";
import type { EmployeeDashboardData } from "@/types/dashboard";
import { PlusOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router";
import "./Dashboard.css";
import { useEffect, useState } from "react";
import { getMyExpenses } from "@/api/expense";
import type { Expense, ExpenseStatus } from "@/types/expense";
import { Button, Table } from "antd";
import type { ColumnsType } from "antd/es/table";
import ExpenseStatusTag from "@/components/ExpenseStatusTag/ExpenseStatusTag";

interface Props {
  data: EmployeeDashboardData;
}

const EmployeeDashboard = ({ data }: Props) => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [expenses, setExpenses] = useState<Expense[]>([]);
  const [isLoadingExpenses, setIsLoadingExpenses] = useState(true);
  useEffect(() => {
    const loadExpenses = async () => {
      try {
        const result = await getMyExpenses();
        setExpenses(result);
      } catch (error) {
        console.error("Failed to load expenses", error);
      } finally {
        setIsLoadingExpenses(false);
      }
    };

    loadExpenses();
  }, []);
  const recentExpenses = expenses.slice(0, 3);

  const statistics = [
    {
      title: "Draft expenses",
      value: data.draftCount,
      description: "Needs completion before submission",
    },
    {
      title: "Submitted",
      value: data.pendingApprovalCount,
      description: "Awaiting review",
    },
    {
      title: "Approved",
      value: data.approvedCount,
      description: "Moving to finance",
    },
    {
      title: "Paid",
      value: data.paidCount,
      description: "Completed reimbursements",
    },
  ];

  const columns: ColumnsType<Expense> = [
    {
      title: "Title",
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
      render: (_, expense) =>
        `${expense.currency} ${expense.amount.toFixed(2)}`,
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      render: (status: ExpenseStatus) => <ExpenseStatusTag status={status} />,
    },
  ];

  const workflowSteps = [
    {
      number: 1,
      title: "Draft",
      description: "Create the request and attach receipts.",
    },
    {
      number: 2,
      title: "Submitted",
      description: "Await manager decision.",
    },
    {
      number: 3,
      title: "Approved",
      description: "Finance validates and pays.",
    },
    {
      number: 4,
      title: "Paid",
      description: "Reimbursement is complete.",
    },
  ];

  return (
    <div className="dashboard-page">
      <div className="dashboard-page__header">
        <div>
          <h1>Good morning, {user?.firstName}</h1>

          <p>Track the status of your reimbursement requests.</p>
        </div>

        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => navigate("/expenses/new")}
        >
          New expense
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

      <div className="dashboard-main-grid">
        <section className="dashboard-panel">
          <div className="dashboard-panel__header">
            <h2>My expense requests</h2>

            <Button type="link" onClick={() => navigate("/expenses")}>
              View all
            </Button>
          </div>

          <Table
            rowKey="id"
            columns={columns}
            dataSource={recentExpenses}
            loading={isLoadingExpenses}
            pagination={false}
            size="middle"
          />
        </section>

        <section className="dashboard-panel">
          <h2 className="dashboard-panel__title">Expense workflow</h2>

          <div className="expense-workflow">
            {workflowSteps.map((step) => (
              <div className="expense-workflow__step" key={step.number}>
                <div className="expense-workflow__number">{step.number}</div>

                <div>
                  <div className="expense-workflow__title">{step.title}</div>

                  <div className="expense-workflow__description">
                    {step.description}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </section>
      </div>
    </div>
  );
};

export default EmployeeDashboard;
