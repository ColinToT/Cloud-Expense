import { Button, Card, Tag, Typography } from "antd";
import { useNavigate, useSearchParams } from "react-router";
import { isUserRole } from "@/types/auth";

const { Paragraph, Title } = Typography;

function DashboardPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const roleValue = searchParams.get("role");
  const role = isUserRole(roleValue) ? roleValue : "EMPLOYEE";

  return (
    <main style={{ padding: 48 }}>
      <Card style={{ maxWidth: 640 }}>
        <Title level={1}>Dashboard</Title>
        <Paragraph>
          This will become the role-aware CloudExpense workspace.
        </Paragraph>

        <Paragraph>
          Prototype role: <Tag color="blue">{role}</Tag>
        </Paragraph>

        <Button onClick={() => navigate("/login")}>Back to login</Button>
      </Card>
    </main>
  );
}

export default DashboardPage;
