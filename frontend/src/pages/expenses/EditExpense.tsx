import { useEffect, useState } from "react";
import {
  Button,
  DatePicker,
  Form,
  Input,
  InputNumber,
  Select,
  Spin,
  message,
} from "antd";
import { ArrowLeftOutlined } from "@ant-design/icons";
import { useNavigate, useParams } from "react-router";
import dayjs from "dayjs";

import { getExpenseById, updateExpense } from "@/api/expense";
import { EXPENSE_CATEGORIES } from "@/constants/expenseCategories";
import type { UpdateExpenseRequest } from "@/types/expense";

import "./ExpenseForm.css";

interface ExpenseFormValues {
  title: string;
  description?: string;
  amount: number;
  currency: string;
  categoryId: number;
  expenseDate: dayjs.Dayjs;
}

const EditExpensePage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm<ExpenseFormValues>();

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (!id) {
      return;
    }

    const loadExpense = async () => {
      try {
        const expense = await getExpenseById(Number(id));

        if (expense.status !== "DRAFT") {
          message.warning("Only draft expenses can be edited.");
          navigate(`/expenses/${id}`);
          return;
        }

        form.setFieldsValue({
          title: expense.title,
          description: expense.description ?? "",
          amount: expense.amount,
          currency: expense.currency,
          categoryId: expense.categoryId,
          expenseDate: dayjs(expense.expenseDate),
        });
      } catch {
        message.error("Failed to load expense.");
      } finally {
        setLoading(false);
      }
    };

    loadExpense();
  }, [id, form, navigate]);

  const handleFinish = async (values: ExpenseFormValues) => {
    if (!id) {
      return;
    }

    const request: UpdateExpenseRequest = {
      title: values.title,
      description: values.description,
      amount: values.amount,
      currency: values.currency,
      categoryId: values.categoryId,
      expenseDate: values.expenseDate.format("YYYY-MM-DD"),
    };

    try {
      setSaving(true);

      await updateExpense(Number(id), request);

      message.success("Expense updated.");

      navigate(`/expenses/${id}`);
    } catch {
      message.error("Failed to update expense.");
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <Spin />;
  }

  return (
    <div className="expense-form-page">
      <Button
        type="text"
        icon={<ArrowLeftOutlined />}
        onClick={() => navigate(`/expenses/${id}`)}
      >
        Back to expense
      </Button>

      <div className="expense-form-page__header">
        <h1>Edit expense</h1>
        <p>Update the details of this expense request.</p>
      </div>

      <div className="expense-form-card">
        <Form form={form} layout="vertical" onFinish={handleFinish}>
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
            <Input />
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
              <Select options={EXPENSE_CATEGORIES} />
            </Form.Item>
          </div>

          <Form.Item label="Description" name="description">
            <Input.TextArea rows={4} />
          </Form.Item>

          <div className="expense-form-actions">
            <Button onClick={() => navigate(`/expenses/${id}`)}>Cancel</Button>

            <Button type="primary" htmlType="submit" loading={saving}>
              Save changes
            </Button>
          </div>
        </Form>
      </div>
    </div>
  );
};

export default EditExpensePage;
