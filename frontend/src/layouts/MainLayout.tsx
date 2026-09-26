import { Breadcrumb, Button, Layout, Menu } from "antd";
import { LogoutOutlined } from "@ant-design/icons";
import { useAuth } from "@/auth/AuthContext";
import { menuConfig } from "@/layouts/menuConfig";
import { Outlet, useLocation, useNavigate } from "react-router";
import "./MainLayout.css";

const { Sider, Header, Content } = Layout;

const Mainlayout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, signOut } = useAuth();
  const menuItems = user ? menuConfig[user.role] : [];

  const selectedMenuItem = menuItems.find(
    (item) => item.path === location.pathname,
  );
  const selectedKeys = selectedMenuItem ? [selectedMenuItem.key] : [];

  const initials = user
    ? `${user.firstName.charAt(0)}${user.lastName.charAt(0)}`
    : "";

  const handleLogout = () => {
    signOut();
    navigate("/login");
  };
  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sider width={244} className="app-sidebar">
        <div className="sidebar-brand">
          <div className="brand-mark">C</div>

          <div>
            <div className="brand-name">CloudExpense</div>
            <div className="brand-subtitle">Expense operations</div>
          </div>
        </div>

        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={selectedKeys}
          items={menuItems.map((item) => ({
            key: item.key,
            label: item.label,
            icon: item.icon,
          }))}
          onClick={({ key }) => {
            const item = menuItems.find((item) => item.key === key);

            if (item) {
              navigate(item.path);
            }
          }}
        />

        <div className="sidebar-user">
          <div className="user-avatar">{initials}</div>

          <div>
            <div className="user-name">
              {user?.firstName} {user?.lastName}
            </div>

            <div className="user-role">{user?.role}</div>
          </div>
        </div>
      </Sider>

      <Layout>
        <Header className="app-header">
          <Breadcrumb
            items={[
              {
                title: "Workspace",
              },
              {
                title: selectedMenuItem?.label ?? "Dashboard",
              },
            ]}
          />

          <Button type="text" icon={<LogoutOutlined />} onClick={handleLogout}>
            Sign out
          </Button>
        </Header>

        <Content className="app-content">
          <div className="content-container">
            <Outlet />
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default Mainlayout;
