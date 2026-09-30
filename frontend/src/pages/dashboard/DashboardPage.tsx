import { useEffect, useState } from "react";
import { Spin } from "antd";
import { getDashboard } from "@/api/dashboard";
import type { DashboardResponse } from "@/types/dashboard";
import EmployeeDashboard from "./EmployeeDashboard";
import ManagerDashboard from "./ManagerDashboard";
import FinanceDashboard from "./FinaceDashboard";

const Dashboard = () => {
  const [dashboard, setDashboard] = useState<DashboardResponse | null>(null);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const data = await getDashboard();
        setDashboard(data);
      } catch (error) {
        console.error("Failed to load dashboard", error);
      }
    };

    loadDashboard();
  }, []);

  if (!dashboard) {
    return <Spin />;
  }

  if (dashboard.role === "EMPLOYEE") {
    return <EmployeeDashboard data={dashboard.data} />;
  }

  if (dashboard.role === "MANAGER") {
    return <ManagerDashboard data={dashboard.data} />;
  }

  if (dashboard.role === "FINANCE") {
    return <FinanceDashboard data={dashboard.data} />;
  }
};

export default Dashboard;
