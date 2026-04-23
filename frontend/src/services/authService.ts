import api from "./api";
import type { LoginPayload, RegisterPayload, User } from "@/types";
import { useAuthStore } from "@/stores/authStore";

export const authService = {

  // Registro — POST /auth
  // Backend espera: { name, email, password }
  // Backend retorna: 201 sem body
  register: async (payload: RegisterPayload): Promise<void> => {
    console.log("Registering user with payload:", payload);
    await api.post("/auth", payload);
  },


  login: async (_payload: LoginPayload) => {
    const response = await api.post("/auth/login", _payload);
    return response.data;
  },

  // Logout — puramente frontend por enquanto
  // Quando tiver refresh token, aqui vai chamar o backend para invalidar
  logout: (): void => {
    useAuthStore.getState().logout();
  },
};