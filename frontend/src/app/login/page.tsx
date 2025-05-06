"use client"; // Marca como Componente de Cliente

import React, { useState, FormEvent } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import api from '@/services/axiosConfig'; // Instância Axios configurada
import axios, { AxiosError } from 'axios';
import Input from '@/components/ui/Input';
import Button from '@/components/ui/Button';

export default function PaginaLogin() {
    const router = useRouter(); // Inicializa o hook do roteador

    // Estados específicos do formulário de login
    const [loginUsuario, setLoginUsuario] = useState(''); // Guarda o nome de usuário digitado
    const [senhaLogin, setSenhaLogin] = useState(''); // Guarda a senha digitada

    // Estados para feedback visual
    const [error, setError] = useState<string | null>(null); // Guarda mensagens de erro
    const [isLoading, setIsLoading] = useState(false); // Controla o estado de carregamento/envio

    // Função chamada ao submeter o formulário
    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault(); // Previne o recarregamento da página padrão do HTML form
        setIsLoading(true); // Ativa o estado de carregamento
        setError(null); // Limpa erros anteriores

        try {
            // Monta o objeto de dados para enviar à API
            const loginData = {
                login: loginUsuario,
                senha: senhaLogin,
            };
            console.log("Enviando dados de login:", loginData);

            // Faz a chamada POST para o endpoint de login do backend
            const response = await api.post('/auth/login', loginData);

            // Verifica se a resposta contém o token esperado
            if (response.data && response.data.token) {
                console.log("Login bem-sucedido, token recebido:", response.data.token);

                // Armazena o token JWT no Local Storage do navegador
                localStorage.setItem('authToken', response.data.token);

                // Configura a instância do Axios para incluir o token em futuras requisições
                api.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;

                // --- REDIRECIONAMENTO ---
                router.push('/dashboard');

            } else {
                // Se a resposta não contiver o token esperado
                throw new Error("Resposta da API de login inválida.");
            }

        } catch (error) { // Tratamento de erros da chamada API
            console.error("Erro no login:", error);
            let errorMessage = 'Erro ao tentar fazer login. Tente novamente mais tarde.';
            // Verifica se é um erro específico do Axios
            if (axios.isAxiosError(error)) {
                const axiosError = error as AxiosError;
                // Verifica se foi um erro de autenticação (401) ou autorização (403)
                if (axiosError.response && (axiosError.response.status === 401 || axiosError.response.status === 403)) {
                    errorMessage = 'Nome de usuário ou senha inválidos.';
                }
                // Futuramente adicionar mais tratamentos para outros erros HTTP aqui
            }
            setError(errorMessage); // Define a mensagem de erro no estado para exibição
        } finally {
            setIsLoading(false); // Desativa o estado de carregamento, independentemente do resultado
        }
    };

    // Estrutura JSX da página
    return (
        <div className="flex min-h-screen bg-gray-900 text-white">
            {/* Coluna Esquerda (Branding) */}
            <div className="hidden lg:flex w-1/2 items-center justify-center bg-gradient-to-br from-gray-800 via-gray-900 to-black p-10">
                <div className="text-center">
                    {/* Adicionar logo futuramente e melhorar o desing */}
                    <h1 className="text-4xl font-bold mb-4">Conecta Pro</h1>
                    <p className="text-xl text-gray-400">Conectando profissionais e clientes.</p>
                </div>
            </div>

            {/* Coluna Direita (Formulário) */}
            <div className="w-full lg:w-1/2 flex items-center justify-center p-8 md:p-12">
                <div className="w-full max-w-md">
                    <h2 className="text-3xl font-bold mb-6 text-center">Login</h2>

                    <form onSubmit={handleSubmit}>

                        {/* Exibição Condicional de Erro */}
                        {error && (
                            <div className="mb-4 p-3 bg-red-900 border border-red-700 text-red-200 rounded-md text-center">
                                {error}
                            </div>
                        )}

                        {/* Campo Nome de Usuário */}
                        <div className="mb-4">
                            <Input
                                id="loginUsuario"
                                label="Nome de usuário"
                                type="text"
                                name="login" // name pode ser útil para autocomplete do navegador
                                autoComplete="username"
                                value={loginUsuario}
                                onChange={(e) => setLoginUsuario(e.target.value)}
                                placeholder="Seu nome de usuário"
                                required
                                disabled={isLoading} // Desabilita durante o carregamento
                            />
                        </div>

                        {/* Campo Senha */}
                        <div className="mb-4">
                            <Input
                                id="senhaLogin"
                                label="Senha"
                                type="password"
                                name="current-password" // name pode ser útil para autocomplete
                                autoComplete="current-password"
                                value={senhaLogin}
                                onChange={(e) => setSenhaLogin(e.target.value)}
                                placeholder="Sua senha"
                                required
                                disabled={isLoading} // Desabilita durante o carregamento
                            />
                        </div>

                        {/* Link Esqueci Senha */}
                        <div className="text-right mb-6">
                            <Link href="/esqueci-senha" // Link para página futura
                                  className={`text-sm text-blue-400 hover:text-blue-300 ${isLoading ? 'pointer-events-none opacity-50' : ''}`}>
                                Esqueceu a senha?
                            </Link>
                        </div>

                        {/* Botão Entrar */}
                        <Button type="submit" isLoading={isLoading} disabled={isLoading}>
                            Entrar
                        </Button>

                        {/* Divisor e Links */}
                        <div className="mt-6 text-center">
                            {/* Divisor */}
                            <div className="relative my-4">
                                <div className="absolute inset-0 flex items-center" aria-hidden="true">
                                    <div className="w-full border-t border-gray-700"></div>
                                </div>
                                <div className="relative flex justify-center text-sm">
                                    <span className="px-2 bg-gray-900 text-gray-500">Ou</span>
                                </div>
                            </div>

                            {/* Botões Social Login (Placeholders) */}
                            <div className="flex justify-center space-x-4 mb-6">
                                <button type="button" aria-label="Login com Google" className="p-2 bg-gray-700 rounded-full hover:bg-gray-600 disabled:opacity-50" disabled={isLoading}>G</button>
                                <button type="button" aria-label="Login com Facebook" className="p-2 bg-gray-700 rounded-full hover:bg-gray-600 disabled:opacity-50" disabled={isLoading}>F</button>
                                <button type="button" aria-label="Login com Apple" className="p-2 bg-gray-700 rounded-full hover:bg-gray-600 disabled:opacity-50" disabled={isLoading}>A</button>
                            </div>

                            {/* Link para Registro */}
                            <p className="text-sm text-gray-400">
                                Não tem uma conta?
                                <Link href="/registrar"
                                      className={`ml-1 text-blue-400 hover:text-blue-300 font-medium ${isLoading ? 'pointer-events-none opacity-50' : ''}`}>
                                    Registre-se
                                </Link>
                            </p>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}