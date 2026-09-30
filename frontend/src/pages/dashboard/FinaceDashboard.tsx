import type { FinanceDashboardData } from "@/types/dashboard";

interface Props {
  data: FinanceDashboardData;
}

const FinanceDashboard = ({ data }: Props) => {
  return <div>Pending payments: {data.pendingPaymentCount}</div>;
};

export default FinanceDashboard;
