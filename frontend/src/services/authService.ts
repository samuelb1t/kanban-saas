import api from "./api";
import type { LoginPayload, RegisterPayload, User } from "@/types";

export const authService = {

  // Registro — POST /auth
  // Backend espera: { name, email, password }
  // Backend retorna: 201 sem body
  register: async (payload: RegisterPayload): Promise<void> => {
    console.log("Registering user with payload:", payload);
    await api.post("/auth", payload);
  },

  // Login — ainda não existe no backend, será implementado com Spring Security
  // Por enquanto mantém mock para o frontend funcionar
  login: async (_payload: LoginPayload): Promise<{ user: User; token: string }> => {
    // TODO: substituir quando o endpoint POST /auth/login existir
    // return await api.post("/auth/login", payload).then(res => res.data);
    await new Promise((r) => setTimeout(r, 500));
    return {
      user: { id: 1, name: "Mock User", email: _payload.email },
      token: "mock-jwt-token",
    };
  },

  // Logout — puramente frontend por enquanto
  // Quando tiver refresh token, aqui vai chamar o backend para invalidar
  logout: (): void => {
    localStorage.removeItem("token");
    // TODO: POST /auth/logout quando tiver refresh token
  },
};