import {
  CheckCircleOutlined,
  EuroOutlined,
  UserOutlined,
} from "@ant-design/icons";
import { Button, Form, Input, Radio, Typography } from "antd";
import { useNavigate } from "react-router";
import type { LoginFormValues } from "@/types/auth";
import "./LoginPage.css";

const { Title, Paragraph } = Typography;

function LoginPage() {
  const navigate = useNavigate();

  function handleFinish({ role }: LoginFormValues) {
    navigate(`/dashboard?role=${role}`);
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
            Choose a prototype role to see its tailored workspace.
          </Paragraph>

          <Form<LoginFormValues>
            layout="vertical"
            initialValues={{ demoRole: "EMPLOYEE" }}
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

            <Form.Item label="Explore as" name="demoRole">
              <Radio.Group className="login-page__role-group">
                <Radio className="login-page__role-option" value="EMPLOYEE">
                  <span className="login-page__role-content">
                    <UserOutlined />
                    <strong>Employee</strong>
                    <small>Submit and track</small>
                  </span>
                </Radio>

                <Radio className="login-page__role-option" value="MANAGER">
                  <span className="login-page__role-content">
                    <CheckCircleOutlined />
                    <strong>Manager</strong>
                    <small>Review and approve</small>
                  </span>
                </Radio>

                <Radio className="login-page__role-option" value="FINANCE">
                  <span className="login-page__role-content">
                    <EuroOutlined />
                    <strong>Finance</strong>
                    <small>Control and pay</small>
                  </span>
                </Radio>
              </Radio.Group>
            </Form.Item>

            <Button block htmlType="submit" size="large" type="primary">
              Sign in
            </Button>
          </Form>
        </section>
      </main>
    </div>
  );
}

export default LoginPage;
