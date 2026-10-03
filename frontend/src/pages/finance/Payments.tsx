import { Button, Form, Input, message, Modal, Select, Table } from "antd";
import type { ColumnsType } from "antd/es/table";
import { useEffect, useState } from "react";

import { getPendingPayments, payExpense } from "@/api/payment";
import type { PaymentRequest, PendingPayment } from "@/types/payment";

import "./Payments.css";

const Payments = () => {
  const [payments, setPayments] = useState<PendingPayment[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedPayment, setSelectedPayment] = useState<PendingPayment | null>(
    null,
  );
  const [paying, setPaying] = useState(false);
  const [form] = Form.useForm<PaymentRequest>();

  const loadPayments = async () => {
    try {
      setLoading(true);

      const data = await getPendingPayments();

      setPayments(data);
    } catch {
      message.error("Failed to load pending payments.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPayments();
  }, []);

  const handlePayment = async () => {
    if (!selectedPayment) {
      return;
    }

    try {
      const values = await form.validateFields();

      setPaying(true);

      await payExpense(selectedPayment.expenseId, values);

      message.success("Payment recorded successfully.");

      setSelectedPayment(null);
      form.resetFields();

      await loadPayments();
    } catch (error) {
      if (error && typeof error === "object" && "errorFields" in error) {
        return;
      }

      message.error("Failed to record payment.");
    } finally {
      setPaying(false);
    }
  };

  const columns: ColumnsType<PendingPayment> = [
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
      render: (_, payment) =>
        `${payment.currency} ${payment.amount.toFixed(2)}`,
    },
    {
      title: "",
      key: "action",
      align: "right",
      render: (_, payment) => (
        <Button type="link" onClick={() => setSelectedPayment(payment)}>
          Pay
        </Button>
      ),
    },
  ];

  return (
    <div className="payments-page">
      <div className="payments-header">
        <h1>Payment queue</h1>
        <p>Process expenses approved by finance.</p>
      </div>

      <div className="payments-card">
        <Table
          rowKey="expenseId"
          columns={columns}
          dataSource={payments}
          loading={loading}
          pagination={false}
        />

        <Modal
          title="Record payment"
          open={selectedPayment !== null}
          onCancel={() => {
            setSelectedPayment(null);
            form.resetFields();
          }}
          onOk={handlePayment}
          okText="Confirm payment"
          confirmLoading={paying}
          destroyOnHidden
        >
          {selectedPayment && (
            <>
              <div className="payment-summary">
                <div>
                  <span>Employee</span>
                  <strong>{selectedPayment.employeeName}</strong>
                </div>

                <div>
                  <span>Expense</span>
                  <strong>{selectedPayment.title}</strong>
                </div>

                <div>
                  <span>Amount</span>
                  <strong>
                    {selectedPayment.currency}{" "}
                    {selectedPayment.amount.toFixed(2)}
                  </strong>
                </div>
              </div>

              <Form form={form} layout="vertical" className="payment-form">
                <Form.Item
                  label="Payment method"
                  name="paymentMethod"
                  rules={[
                    {
                      required: true,
                      message: "Please select a payment method.",
                    },
                  ]}
                >
                  <Select
                    placeholder="Select payment method"
                    options={[
                      {
                        label: "Bank transfer",
                        value: "BANK_TRANSFER",
                      },
                      {
                        label: "Cash",
                        value: "CASH",
                      },
                      {
                        label: "Other",
                        value: "OTHER",
                      },
                    ]}
                  />
                </Form.Item>

                <Form.Item
                  label="Transaction reference"
                  name="transactionReference"
                >
                  <Input placeholder="e.g. TXN-20261003-001" />
                </Form.Item>
              </Form>
            </>
          )}
        </Modal>
      </div>
    </div>
  );
};

export default Payments;
