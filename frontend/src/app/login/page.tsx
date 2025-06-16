// frontend/src/app/login/page.tsx
"use client";

import React, { useState, FormEvent } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import Link from 'next/link';
import { signIn } from 'next-auth/react'; // <-- Importa a função signIn!
import Input from '@/components/ui/Input';
import Button from '@/components/ui/Button';

export default function PaginaLogin() {
    const router = useRouter();
    const searchParams = useSearchParams();
    const callbackUrl = searchParams.get('callbackUrl') || '/dashboard';

    const [login, setLogin] = useState(''); // Campo para nome de usuário
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setIsLoading(true);
        setError(null);

        try {
            // --- ESTA É A LÓGICA CORRETA ---
            // Usamos a função signIn do NextAuth para chamar nosso provider 'credentials'
            const result = await signIn('credentials', {
                redirect: false, // Não redireciona automaticamente para podermos tratar o resultado
                login: login, // Passa o 'login' que o nosso 'authorize' espera
                password: password,
            });

            setIsLoading(false);

            if (result?.error) {
                // Se NextAuth retornar um erro (ex: credenciais inválidas)
                console.error("Erro de autenticação retornado pelo NextAuth:", result.error);
                setError("Nome de usuário ou senha inválidos.");
            } else if (result?.ok) {
                // Se o login for bem-sucedido, redirecionamos para o dashboard
                console.log("Login via NextAuth bem-sucedido, redirecionando...");
                router.push(callbackUrl);
            }

        } catch (error) {
            console.error("Erro inesperado no processo de login:", error);
            setIsLoading(false);
            setError("Ocorreu um erro inesperado. Tente novamente.");
        }
    };

    return (
        <div className="flex min-h-screen bg-gray-900 text-white">
            {/* Coluna Esquerda */}
            <div className="hidden lg:flex w-1/2 items-center justify-center bg-gradient-to-br from-gray-800 via-gray-900 to-black p-10">
                <div className="text-center">
                    <svg className="mx-auto h-24 w-24 mb-6 text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" /></svg>
                    <h1 className="text-4xl font-bold mb-4">Conecta Pro</h1>
                    <p className="text-xl text-gray-400">Conectando profissionais e clientes.</p>
                </div>
            </div>

            {/* Coluna Direita (Formulário de Login) */}
            <div className="w-full lg:w-1/2 flex items-center justify-center p-8 md:p-12">
                <div className="w-full max-w-md">
                    <h2 className="text-3xl font-bold mb-6 text-center">Login</h2>
                    <form onSubmit={handleSubmit}>
                        {error && (
                            <div className="mb-4 p-3 bg-red-900 border border-red-700 text-red-200 rounded-md text-center">
                                {error}
                            </div>
                        )}
                        <div className="mb-4">
                            <Input
                                id="login"
                                label="Nome de usuário"
                                type="text"
                                value={login}
                                onChange={(e) => setLogin(e.target.value)}
                                placeholder="Seu nome de usuário"
                                required
                                disabled={isLoading}
                            />
                        </div>
                        <div className="mb-4">
                            <Input
                                id="password"
                                label="Senha"
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="Sua senha"
                                required
                                disabled={isLoading}
                            />
                        </div>
                        <div className="text-right mb-6">
                            <Link href="/esqueci-senha" className={`text-sm text-blue-400 hover:text-blue-300 ${isLoading ? 'pointer-events-none' : ''}`}>
                                Esqueceu a senha?
                            </Link>
                        </div>
                        <Button type="submit" isLoading={isLoading} disabled={isLoading || !login || !password}>
                            {isLoading ? 'Entrando...' : 'Entrar'}
                        </Button>
                        <div className="mt-6 text-center">
                            <p className="mt-6 text-sm text-gray-400">
                                Não tem uma conta?
                                <Link href="/registrar" className={`ml-1 text-blue-400 hover:text-blue-300 font-medium ${isLoading ? 'pointer-events-none' : ''}`}>
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