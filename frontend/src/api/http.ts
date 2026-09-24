import axios from "axios";
import { getAccessToken } from "@/auth/token";

export const http = axios.create({
  baseURL: "/api",
  timeout: 10_000,
  headers: {
    "Content-Type": "application/json",
  },
});

http.interceptors.request.use((config) => {
  const accessToken = getAccessToken();

  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});