import { Button, Result } from "antd";
import { useNavigate } from "react-router";

const NotFound = () => {
  const navigate = useNavigate();

  return (
    <Result
      status="404"
      title="404"
      subTitle="Sorry, the page you requested could not be found."
      extra={
        <Button type="primary" onClick={() => navigate("/dashboard")}>
          Back to dashboard
        </Button>
      }
    />
  );
};

export default NotFound;
