import type { ManagerDashboardData } from "@/types/dashboard";

interface Props {
  data: ManagerDashboardData;
}

const ManagerDashboard = ({ data }: Props) => {
  return <div>Pending approvals: {data.pendingApprovalCount}</div>;
};

export default ManagerDashboard;
