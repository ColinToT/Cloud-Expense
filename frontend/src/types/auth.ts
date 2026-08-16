export const USER_ROLES = ['EMPLOYEE', 'MANAGER', 'FINANCE'] as const

export type UserRole = (typeof USER_ROLES)[number]

export interface LoginFormValues {
  email: string
  password: string
  role: UserRole
}

export function isUserRole(value: string | null): value is UserRole {
    return USER_ROLES.some(role => role === value)
}