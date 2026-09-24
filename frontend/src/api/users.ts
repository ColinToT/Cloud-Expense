import type { CurrentUser } from "@/types/auth";
import { http } from "./http";

export async function getCurrentUser(): Promise<CurrentUser> {
  const response = await http.get<CurrentUser>("/users/me");

  return response.data;
}
