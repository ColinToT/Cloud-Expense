import { useState } from "react";
import {
  Button,
  DatePicker,
  Form,
  Input,
  InputNumber,
  Select,
  message,
} from "antd";
import { ArrowLeftOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router";
import dayjs from "dayjs";

import { createExpense } from "@/api/expense";
import type { CreateExpenseRequest } from "@/types/expense";
import { EXPENSE_CATEGORIES } from "@/constants/expenseCategories";

import "./ExpenseForm.css";

interface ExpenseFormValues {
  title: string;
  description?: string;
  amount: number;
  currency: string;
  categoryId: number;
  expenseDate: dayjs.Dayjs;
}

const NewExpensePage = () => {
  const navigate = useNavigate();
  const [submitting, setSubmitting] = useState(false);

  const handleFinish = async (values: ExpenseFormValues) => {
    const request: CreateExpenseRequest = {
      title: values.title,
      description: values.description,
      amount: values.amount,
      currency: values.currency,
      categoryId: values.categoryId,
      expenseDate: values.expenseDate.format("YYYY-MM-DD"),
    };

    try {
      setSubmitting(true);

      const expense = await createExpense(request);

      message.success("Expense created.");

      navigate(`/expenses/${expense.id}`);
    } catch {
      message.error("Failed to create expense.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="expense-form-page">
      <Button
        type="text"
        icon={<ArrowLeftOutlined />}
        onClick={() => navigate("/expenses")}
      >
        Back to expenses
      </Button>

      <div className="expense-form-page__header">
        <h1>New expense</h1>
        <p>Create a reimbursement request and attach your receipt.</p>
      </div>

      <div className="expense-form-card">
        <Form<ExpenseFormValues>
          layout="vertical"
          onFinish={handleFinish}
          initialValues={{
            currency: "EUR",
          }}
        >
          <Form.Item
            label="Title"
            name="title"
            rules={[
              {
                required: true,
                message: "Please enter an expense title.",
              },
            ]}
          >
            <Input placeholder="e.g. Client dinner" />
          </Form.Item>

          <div className="expense-form-grid">
            <Form.Item
              label="Amount"
              name="amount"
              rules={[
                {
                  required: true,
                  message: "Please enter an amount.",
                },
              ]}
            >
              <InputNumber min={0.01} precision={2} style={{ width: "100%" }} />
            </Form.Item>

            <Form.Item
              label="Currency"
              name="currency"
              rules={[{ required: true }]}
            >
              <Select
                options={[
                  { value: "EUR", label: "EUR" },
                  { value: "GBP", label: "GBP" },
                  { value: "USD", label: "USD" },
                ]}
              />
            </Form.Item>

            <Form.Item
              label="Expense date"
              name="expenseDate"
              rules={[
                {
                  required: true,
                  message: "Please select the expense date.",
                },
              ]}
            >
              <DatePicker style={{ width: "100%" }} />
            </Form.Item>

            <Form.Item
              label="Category"
              name="categoryId"
              rules={[
                {
                  required: true,
                  message: "Please select a category.",
                },
              ]}
            >
              <Select
                placeholder="Select category"
                options={EXPENSE_CATEGORIES}
              />
            </Form.Item>
          </div>

          <Form.Item label="Description" name="description">
            <Input.TextArea
              rows={4}
              placeholder="Describe the business purpose of this expense"
            />
          </Form.Item>

          <div className="expense-form-actions">
            <Button onClick={() => navigate("/expenses")}>Cancel</Button>

            <Button type="primary" htmlType="submit" loading={submitting}>
              Create expense
            </Button>
          </div>
        </Form>
      </div>
    </div>
  );
};

export default NewExpensePage;
