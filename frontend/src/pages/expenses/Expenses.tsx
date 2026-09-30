import { useEffect, useState } from "react";
import { getMyExpenses } from "@/api/expense";
import type { Expense, ExpenseStatus } from "@/types/expense";
import { Button, Input, Select, Table } from "antd";
import { PlusOutlined, SearchOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router";
import type { ColumnsType } from "antd/es/table";
import "./Expenses.css";
import ExpenseStatusTag from "@/components/ExpenseStatusTag/ExpenseStatusTag";

const Expenses = () => {
  const [expenses, setExpenses] = useState<Expense[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchText, setSearchText] = useState("");
  const [status, setStatus] = useState<ExpenseStatus | "ALL">("ALL");
  const navigate = useNavigate();

  useEffect(() => {
    const loadExpenses = async () => {
      try {
        const data = await getMyExpenses();
        setExpenses(data);
      } catch (error) {
        console.error("Failed to load expenses", error);
      } finally {
        setLoading(false);
      }
    };

    loadExpenses();
  }, []);

  const filteredExpenses = expenses.filter((expense) => {
    const matchesSearch = expense.title
      .toLowerCase()
      .includes(searchText.toLowerCase());

    const matchesStatus = status === "ALL" || expense.status === status;

    return matchesSearch && matchesStatus;
  });

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
    {
      title: "",
      key: "action",
      render: (_, expense) => (
        <Button type="link" onClick={() => navigate(`/expenses/${expense.id}`)}>
          View
        </Button>
      ),
    },
  ];

  return (
    <div className="expenses-page">
      <div className="expenses-page__header">
        <div>
          <h1>My expenses</h1>
          <p>Track and manage your reimbursement requests.</p>
        </div>

        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => navigate("/expenses/new")}
        >
          New expense
        </Button>
      </div>

      <div className="expenses-toolbar">
        <Input
          prefix={<SearchOutlined />}
          placeholder="Search expenses"
          value={searchText}
          onChange={(event) => setSearchText(event.target.value)}
          allowClear
        />

        <Select
          value={status}
          onChange={setStatus}
          options={[
            { value: "ALL", label: "All statuses" },
            { value: "DRAFT", label: "Draft" },
            { value: "SUBMITTED", label: "Submitted" },
            { value: "MANAGER_APPROVED", label: "Manager approved" },
            { value: "FINANCE_APPROVED", label: "Finance approved" },
            { value: "REJECTED", label: "Rejected" },
            { value: "PAID", label: "Paid" },
          ]}
        />
      </div>

      <div className="expenses-table-card">
        <Table
          rowKey="id"
          columns={columns}
          dataSource={filteredExpenses}
          loading={loading}
          pagination={{ pageSize: 8 }}
        />
      </div>
    </div>
  );
};

export default Expenses;
