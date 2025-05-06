import axios from 'axios';

// Cria uma instância do axios com a URL base configurada
const api = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_BASE_URL, // Lê a variável de ambiente
});

// precisa adicionar a lógica para incluir o token JWT automaticamente
// nos cabeçalhos das requisições para endpoints protegidos.
// api.interceptors.request.use(config => { ... });

export default api;