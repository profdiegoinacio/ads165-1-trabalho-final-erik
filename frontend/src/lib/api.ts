// O caminho completo do arquivo deve ser: frontend/src/lib/api.ts
import axios from 'axios';

const api = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_BASE_URL,
});

// Futuramente, adicionaremos interceptors aqui para injetar o token JWT
// em todas as requisições autenticadas.

export default api;