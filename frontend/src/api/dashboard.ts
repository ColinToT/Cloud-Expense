import { http } from "@/api/http";
import type { DashboardResponse } from "@/types/dashboard";

export const getDashboard = async (): Promise<DashboardResponse> => {
  const response = await http.get<DashboardResponse>("/dashboard");
  return response.data;
};
