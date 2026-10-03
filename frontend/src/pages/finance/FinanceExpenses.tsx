import { Button, Table, message } from "antd";
import type { ColumnsType } from "antd/es/table";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";

import { getFinanceExpenses } from "@/api/finance";
import type { FinanceExpense } from "@/types/finance";

import "./FinanceExpenses.css";

const FinanceExpenses = () => {
  const navigate = useNavigate();

  const [expenses, setExpenses] = useState<FinanceExpense[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadExpenses = async () => {
      try {
        const data = await getFinanceExpenses();
        setExpenses(data);
      } catch {
        message.error("Failed to load finance expenses.");
      } finally {
        setLoading(false);
      }
    };

    loadExpenses();
  }, []);

  const columns: ColumnsType<FinanceExpense> = [
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
          onClick={(event) => {
            event.stopPropagation();

            navigate(`/finance/expenses/${expense.id}`);
          }}
        >
          Review
        </Button>
      ),
    },
  ];

  return (
    <div className="finance-expenses-page">
      <div className="finance-expenses-header">
        <div>
          <h1>Finance review</h1>
          <p>Review expenses and complete finance approval.</p>
        </div>
      </div>

      <div className="finance-expenses-card">
        <Table
          rowKey="id"
          columns={columns}
          dataSource={expenses}
          loading={loading}
          pagination={false}
          onRow={(expense) => ({
            onClick: () => navigate(`/finance/expenses/${expense.id}`),
          })}
        />
      </div>
    </div>
  );
};

export default FinanceExpenses;
