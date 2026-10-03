import { useEffect, useState } from "react";
import { Button, Table } from "antd";
import type { ColumnsType } from "antd/es/table";
import { useNavigate } from "react-router";

import { useAuth } from "@/auth/AuthContext";
import { getFinanceExpenses } from "@/api/finance";
import { getPendingPayments } from "@/api/payment";

import type { FinanceDashboardData } from "@/types/dashboard";
import type { FinanceExpense } from "@/types/finance";
import type { PendingPayment } from "@/types/payment";

import "./Dashboard.css";

interface Props {
  data: FinanceDashboardData;
}

const FinanceDashboard = ({ data }: Props) => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [expenses, setExpenses] = useState<FinanceExpense[]>([]);
  const [payments, setPayments] = useState<PendingPayment[]>([]);

  const [isLoadingExpenses, setIsLoadingExpenses] = useState(true);
  const [isLoadingPayments, setIsLoadingPayments] = useState(true);

  useEffect(() => {
    const loadExpenses = async () => {
      try {
        const result = await getFinanceExpenses();
        setExpenses(result);
      } catch (error) {
        console.error("Failed to load finance expenses", error);
      } finally {
        setIsLoadingExpenses(false);
      }
    };

    const loadPayments = async () => {
      try {
        const result = await getPendingPayments();
        setPayments(result);
      } catch (error) {
        console.error("Failed to load pending payments", error);
      } finally {
        setIsLoadingPayments(false);
      }
    };

    void loadExpenses();
    void loadPayments();
  }, []);

  const recentExpenses = expenses.slice(0, 3);
  const recentPayments = payments.slice(0, 3);

  const statistics = [
    {
      title: "Awaiting review",
      count: data.pendingApprovalCount,
      amount: data.pendingApprovalAmount,
      description: "Expenses requiring finance approval",
    },
    {
      title: "Pending payment",
      count: data.pendingPaymentCount,
      amount: data.pendingPaymentAmount,
      description: "Approved expenses awaiting payment",
    },
    {
      title: "Paid",
      count: data.paidCount,
      amount: data.paidAmount,
      description: "Completed reimbursements",
    },
  ];

  const expenseColumns: ColumnsType<FinanceExpense> = [
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
      render: (_, expense) =>
        `${expense.currency} ${expense.amount.toFixed(2)}`,
    },
    {
      title: "Date",
      dataIndex: "expenseDate",
      key: "expenseDate",
    },
    {
      title: "",
      key: "action",
      align: "right",
      render: (_, expense) => (
        <Button
          type="link"
          onClick={() => navigate(`/finance/expenses/${expense.id}`)}
        >
          Review
        </Button>
      ),
    },
  ];

  const paymentColumns: ColumnsType<PendingPayment> = [
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
      render: (_, payment) =>
        `${payment.currency} ${payment.amount.toFixed(2)}`,
    },
    {
      title: "Date",
      dataIndex: "expenseDate",
      key: "expenseDate",
    },
  ];

  return (
    <div className="dashboard-page">
      <div className="dashboard-page__header">
        <div>
          <h1>Good morning, {user?.firstName}</h1>
          <p>Manage expense reviews and reimbursement payments.</p>
        </div>
        <div className="dashboard-page__actions">
          <Button type="primary" onClick={() => navigate("/finance/expenses")}>
            Review expenses
          </Button>

          <Button type="primary" onClick={() => navigate("/payments")}>
            Process payments
          </Button>
        </div>
      </div>

      <div className="dashboard-stats dashboard-stats--three">
        {statistics.map((stat) => (
          <div className="dashboard-stat-card" key={stat.title}>
            <div className="dashboard-stat-card__title">{stat.title}</div>

            <div className="dashboard-stat-card__value">{stat.count}</div>

            <div className="dashboard-stat-card__amount">
              €{stat.amount.toFixed(2)}
            </div>

            <div className="dashboard-stat-card__description">
              {stat.description}
            </div>
          </div>
        ))}
      </div>

      <div className="dashboard-finance-grid">
        <section className="dashboard-panel">
          <div className="dashboard-panel__header">
            <h2>Expenses awaiting review</h2>

            <Button
              type="primary"
              onClick={() => navigate("/finance/expenses")}
            >
              View all
            </Button>
          </div>

          <Table
            rowKey="id"
            columns={expenseColumns}
            dataSource={recentExpenses}
            loading={isLoadingExpenses}
            pagination={false}
            size="middle"
          />
        </section>

        <section className="dashboard-panel">
          <div className="dashboard-panel__header">
            <h2>Pending payments</h2>

            <Button type="primary" onClick={() => navigate("/payments")}>
              View all
            </Button>
          </div>

          <Table
            rowKey="expenseId"
            columns={paymentColumns}
            dataSource={recentPayments}
            loading={isLoadingPayments}
            pagination={false}
            size="middle"
          />
        </section>
      </div>
    </div>
  );
};

export default FinanceDashboard;
