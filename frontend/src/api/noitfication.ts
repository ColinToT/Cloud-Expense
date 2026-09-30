import { http } from "@/api/http";

export const getUnreadNotificationCount = async (): Promise<number> => {
  const response = await http.get<number>("/notifications/unread-count");
  return response.data;
};
