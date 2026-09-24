import { Button, Form, Input, message, Typography } from "antd";
import { useNavigate } from "react-router";
import type { LoginFormValues } from "@/types/auth";
import "./LoginPage.css";
import { useState } from "react";
import { useAuth } from "@/auth/AuthContext";

const { Title, Paragraph } = Typography;

const DEMO_ACCOUNTS = [
  { role: "Employee", email: "employee@test.com", password: "password123" },
  { role: "Manager", email: "manager@test.com", password: "password123" },
  { role: "Finance", email: "finance@test.com", password: "password123" },
] as const;

function LoginPage() {
  const { signIn } = useAuth();
  const navigate = useNavigate();

  const [form] = Form.useForm<LoginFormValues>();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [selectedDemoRole, setSelectedDemoRole] = useState<string | null>(
    "Employee",
  );

  function selectDemoAccount(account: (typeof DEMO_ACCOUNTS)[number]) {
    form.setFieldsValue({
      email: account.email,
      password: account.password,
    });
    setSelectedDemoRole(account.role);
  }

  async function handleFinish(values: LoginFormValues) {
    setIsSubmitting(true);
    try {
      const currentUser = await signIn(values);

      message.success(`Welcome back, ${currentUser.firstName}.`);
      navigate("/dashboard", { replace: true });
    } catch {
      message.error("Sign in failed. Check your email and password.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="login-page">
      <aside className="login-page__hero">
        <div className="login-page__brand">
          <span className="login-page__brand-mark">C</span>
          <span>
            CloudExpense
            <small>Expense operations</small>
          </span>
        </div>

        <div className="login-page__hero-copy">
          <Paragraph className="login-page__eyebrow">
            EXPENSES, MADE SIMPLE
          </Paragraph>
          <Title className="login-page__hero-title">
            More clarity.
            <br />
            <span>Less chasing.</span>
          </Title>
          <Paragraph>
            One calm place for employees, managers and finance teams to move
            spending forward.
          </Paragraph>
        </div>
      </aside>

      <main className="login-page__content">
        <section className="login-page__form-panel">
          <Paragraph className="login-page__eyebrow">WELCOME BACK</Paragraph>
          <Title level={1}>Sign in to CloudExpense</Title>
          <Paragraph type="secondary">
            Sign in with your CloudExpense work account.
          </Paragraph>

          <Form<LoginFormValues>
            form={form}
            layout="vertical"
            onFinish={handleFinish}
          >
            <Form.Item
              label="Work email"
              name="email"
              rules={[
                { required: true, message: "Enter your work email." },
                { type: "email", message: "Enter a valid email address." },
              ]}
            >
              <Input size="large" placeholder="name@company.com" />
            </Form.Item>

            <Form.Item
              label="Password"
              name="password"
              rules={[{ required: true, message: "Enter your password." }]}
            >
              <Input.Password size="large" placeholder="Enter your password" />
            </Form.Item>

            <Button
              block
              htmlType="submit"
              loading={isSubmitting}
              size="large"
              type="primary"
            >
              Sign in
            </Button>

            <div className="login-page__demo-accounts">
              <Paragraph className="login-page__demo-title">
                DEMO ACCOUNTS
              </Paragraph>
              <Paragraph type="secondary" className="login-page__demo-copy">
                Choose an account to fill in the sign-in details.
              </Paragraph>
              <div className="login-page__demo-options">
                {DEMO_ACCOUNTS.map((account) => (
                  <Button
                    aria-pressed={selectedDemoRole === account.role}
                    className="login-page__demo-option"
                    key={account.role}
                    onClick={() => selectDemoAccount(account)}
                    type={
                      selectedDemoRole === account.role ? "primary" : "default"
                    }
                  >
                    {account.role}
                  </Button>
                ))}
              </div>
            </div>
          </Form>
        </section>
      </main>
    </div>
  );
}

export default LoginPage;
