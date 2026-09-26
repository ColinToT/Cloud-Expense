import type { ReactNode } from "react";
import type { UserRole } from "@/types/auth";
import {
  BellOutlined,
  CheckSquareOutlined,
  CreditCardOutlined,
  FileAddOutlined,
  FileTextOutlined,
  PieChartOutlined,
  ProfileOutlined,
} from "@ant-design/icons";

export interface AppMenuItem {
  key: string;
  label: string;
  path: string;
  icon: ReactNode;
}

export const menuConfig: Record<UserRole, AppMenuItem[]> = {
  EMPLOYEE: [
    {
      key: "dashboard",
      label: "Dashboard",
      path: "/dashboard",
      icon: <PieChartOutlined />,
    },
    {
      key: "expenses",
      label: "My expenses",
      path: "/expenses",
      icon: <ProfileOutlined />,
    },
    {
      key: "new-expense",
      label: "New expense",
      path: "/expenses/new",
      icon: <FileAddOutlined />,
    },
    {
      key: "reports",
      label: "My reports",
      path: "/reports",
      icon: <FileTextOutlined />,
    },
    {
      key: "notifications",
      label: "Notifications",
      path: "/notifications",
      icon: <BellOutlined />,
    },
  ],

  MANAGER: [
    {
      key: "dashboard",
      label: "Dashboard",
      path: "/dashboard",
      icon: <PieChartOutlined />,
    },
    {
      key: "approvals",
      label: "Pending approvals",
      path: "/approvals",
      icon: <CheckSquareOutlined />,
    },
    {
      key: "reports",
      label: "Team reports",
      path: "/reports",
      icon: <FileTextOutlined />,
    },
    {
      key: "notifications",
      label: "Notifications",
      path: "/notifications",
      icon: <BellOutlined />,
    },
  ],

  FINANCE: [
    {
      key: "dashboard",
      label: "Dashboard",
      path: "/dashboard",
      icon: <PieChartOutlined />,
    },
    {
      key: "finance-review",
      label: "Finance review",
      path: "/finance/expenses",
      icon: <CheckSquareOutlined />,
    },
    {
      key: "payments",
      label: "Payment queue",
      path: "/payments",
      icon: <CreditCardOutlined />,
    },
    {
      key: "reports",
      label: "Expense reports",
      path: "/reports",
      icon: <FileTextOutlined />,
    },
    {
      key: "notifications",
      label: "Notifications",
      path: "/notifications",
      icon: <BellOutlined />,
    },
  ],
};
