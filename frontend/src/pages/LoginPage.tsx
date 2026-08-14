import { Button, Card, Typography } from "antd";
import { useNavigate } from "react-router";

const { Title, Paragraph } = Typography;

function LoginPage() {
  const navigate = useNavigate();

  return (
    <main style={{ padding: 48 }}>
      <Card style={{ maxWidth: 480 }}>
        <Title level={1}>CloudExpense</Title>
        <Paragraph>Sign in to manage your expenses.</Paragraph>
        <Button type="primary" onClick={() => navigate("/dashboard")}>
          Prototype sign in
        </Button>
      </Card>
    </main>
  );
}

export default LoginPage;
