import { http } from "@/api/http";
import type { Notification } from "@/types/notification";

export const getUnreadNotificationCount = async (): Promise<number> => {
  const response = await http.get<number>("/notifications/unread-count");
  return response.data;
};

export const getNotifications = async (): Promise<Notification[]> => {
  const response = await http.get<Notification[]>("/notifications");
  return response.data;
};

export const markNotificationAsRead = async (id: number): Promise<void> => {
  await http.put(`/notifications/${id}/read`);
};
