import type { LoginPayload, RegisterPayload, User } from "@/types";
import { mockUser } from "./mockData";

// Mock implementations — will be replaced with real API calls
export const authService = {
  login: async (_payload: LoginPayload): Promise<{ user: User; token: string }> => {
    await new Promise((r) => setTimeout(r, 500));
    return { user: mockUser, token: "mock-jwt-token" };
  },

  register: async (_payload: RegisterPayload): Promise<{ user: User; token: string }> => {
    await new Promise((r) => setTimeout(r, 500));
    return { user: { ...mockUser, name: _payload.name, email: _payload.email }, token: "mock-jwt-token" };
  },

  logout: async (): Promise<void> => {
    localStorage.removeItem("token");
  },
};
