import { Empty, message, Spin } from "antd";
import { useEffect, useState } from "react";

import type { Notification } from "@/types/notification";

import "./Notifications.css";
import { getNotifications, markNotificationAsRead } from "@/api/notification";
import { useNavigate } from "react-router";
import { useAuth } from "@/auth/AuthContext";

const Notifications = () => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const navigate = useNavigate();
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);

  const loadNotifications = async () => {
    try {
      setLoading(true);

      const data = await getNotifications();

      setNotifications(data);
    } catch {
      message.error("Failed to load notifications.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadNotifications();
  }, []);

  const handleNotificationClick = async (notification: Notification) => {
    try {
      if (!notification.readStatus) {
        await markNotificationAsRead(notification.id);

        setNotifications((current) =>
          current.map((item) =>
            item.id === notification.id
              ? {
                  ...item,
                  readStatus: true,
                }
              : item,
          ),
        );
      }

      navigateToReference(notification);
    } catch {
      message.error("Failed to open notification.");
    }
  };

  const navigateToReference = (notification: Notification) => {
    if (!notification.referenceId) {
      return;
    }

    if (notification.type === "EXPENSE_SUBMITTED" && user?.role === "MANAGER") {
      navigate(`/approvals/${notification.referenceId}`);
      return;
    }

    if (user?.role === "EMPLOYEE") {
      navigate(`/expenses/${notification.referenceId}`);
      return;
    }

    if (notification.type === "EXPENSE_APPROVED" && user?.role === "FINANCE") {
      navigate(`/finance/expenses/${notification.referenceId}`);
    }
  };

  const formatNotificationTime = (createdAt: string) =>
    new Intl.DateTimeFormat("en-IE", {
      day: "numeric",
      month: "short",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    }).format(new Date(createdAt));

  if (loading) {
    return <Spin />;
  }

  return (
    <div className="notifications-page">
      <div className="notifications-header">
        <h1>Notifications</h1>
        <p>Stay up to date with your expense activity.</p>
      </div>

      <div className="notifications-card">
        {notifications.length === 0 ? (
          <Empty description="No notifications" />
        ) : (
          notifications.map((notification) => (
            <button
              type="button"
              key={notification.id}
              className={`notification-item ${
                notification.readStatus ? "" : "notification-item--unread"
              }`}
              onClick={() => handleNotificationClick(notification)}
            >
              <span className="notification-item__indicator" />

              <div className="notification-item__content">
                <div className="notification-item__header">
                  <strong>{notification.title}</strong>

                  <span>{formatNotificationTime(notification.createdAt)}</span>
                </div>

                <p>{notification.message}</p>
              </div>
            </button>
          ))
        )}
      </div>
    </div>
  );
};

export default Notifications;
