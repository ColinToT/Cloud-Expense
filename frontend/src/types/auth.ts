export const USER_ROLES = ["EMPLOYEE", "MANAGER", "FINANCE"] as const;

export type UserRole = (typeof USER_ROLES)[number];

export interface LoginFormValues {
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
}

export interface CurrentUser {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
}

export function isUserRole(value: string | null): value is UserRole {
  return USER_ROLES.some((role) => role === value);
}
