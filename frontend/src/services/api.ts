import axios from "axios";
import { useAuthStore } from "@/stores/authStore";

// Instância central do Axios — todos os services usam essa, não axios direto
const api = axios.create({
  baseURL: "http://localhost:8080", // base do backend
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor de requisição:
// Antes de cada chamada, pega o token do store e injeta no header
// Quando tiver JWT, isso vai funcionar automaticamente em toda a app
api.interceptors.request.use((config) => {
  const token = useAuthStore.getState().token;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor de resposta:
// Se o backend retornar 401, desloga o usuário automaticamente
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      useAuthStore.getState().logout();
    }
    return Promise.reject(error);
  }
);

export default api;