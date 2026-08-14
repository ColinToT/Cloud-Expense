import { Button, Card, Typography } from "antd";
import { useNavigate } from "react-router";

const { Title, Paragraph } = Typography;

function DashboardPage() {
  const navigate = useNavigate();

  return (
    <main style={{ padding: 48 }}>
      <Card style={{ maxWidth: 640 }}>
        <Title level={1}>Dashboard</Title>
        <Paragraph>
          This will become the role-aware CloudExpense workspace.
        </Paragraph>

        <Button onClick={() => navigate("/login")}>Back to login</Button>
      </Card>
    </main>
  );
}

export default DashboardPage;
