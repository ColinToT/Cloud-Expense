import type { LoginRequest, LoginResponse } from '@/types/auth'
import {http} from '@/api/http'

export async function login(payload: LoginRequest): Promise<LoginResponse> {
    const response = await http.post<LoginResponse>('auth/login', payload)
    return response.data
}