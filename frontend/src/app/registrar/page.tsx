"use client";

import React, { useState, FormEvent } from 'react';
import { useRouter } from 'next/navigation'; // Pode ser usado para redirecionar após registro
import Link from 'next/link'; // Import do Link do Next.js para navegação
import api from '@/services/axiosConfig'; // Importa nossa instância axios (ajuste o path se necessário)
import axios, { AxiosError } from 'axios'; // Import AxiosError para tipagem de erro
import Input from '@/components/ui/Input'; // Importa o componente Input
import Button from '@/components/ui/Button'; // Importa o componente Button

// Interface para o tipo de erro esperado da API (pode mover para um arquivo de tipos)
interface ApiErrorData {
    message?: string;
}

export default function PaginaRegistro() { // Renomeado de volta
    const router = useRouter();

    // --- Estados para os Campos do Formulário de Registro ---
    const [nome, setNome] = useState('');
    const [email, setEmail] = useState('');
    const [nomeUsuarioReg, setNomeUsuarioReg] = useState('');
    const [telefone, setTelefone] = useState('');
    const [senhaReg, setSenhaReg] = useState('');
    const [confirmarSenha, setConfirmarSenha] = useState('');
    const [termos, setTermos] = useState(false);

    // Estado para mensagens de erro e loading
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);

    // Função para lidar com o envio do formulário de registro
    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setIsLoading(true);
        setError(null);

        // Validação simples de senha
        if (senhaReg !== confirmarSenha) {
            setError('As senhas não coincidem!');
            setIsLoading(false);
            return;
        }
        if (!termos) {
            setError('Você precisa aceitar os termos de serviço.');
            setIsLoading(false);
            return;
        }

        try {
            const registroData = {
                nome,
                email,
                nomeUsuario: nomeUsuarioReg, // Garante que o nome do campo corresponde ao DTO do backend
                telefone: telefone || null, // Envia null se vazio
                senha: senhaReg,
            };
            console.log("Enviando dados de registro:", registroData); // Debug
            await api.post('/usuarios/registrar', registroData); // Chama API de registro

            alert('Registro realizado com sucesso! Faça o login para continuar.'); // Feedback temporário
            // Limpa o formulário (opcional)
            setNome(''); setEmail(''); setNomeUsuarioReg(''); setTelefone('');
            setSenhaReg(''); setConfirmarSenha(''); setTermos(false);
            // Redireciona para a página de login após sucesso
            router.push('/login');

        } catch (error) { // Tratamento de erro tipado
            console.error("Erro no registro:", error);
            let errorMessage = 'Erro desconhecido ao registrar. Tente novamente mais tarde.';
            if (axios.isAxiosError(error)) {
                const axiosError = error as AxiosError<string | ApiErrorData>;
                if (axiosError.response?.data) {
                    const errorData = axiosError.response.data;
                    if (typeof errorData === 'string' && errorData.includes("Erro:")) {
                        errorMessage = errorData;
                    } else if (typeof errorData === 'object' && errorData?.message) {
                        errorMessage = errorData.message;
                    } else {
                        errorMessage = 'Erro ao registrar. Verifique os dados e tente novamente.';
                    }
                } else if (axiosError.message) {
                    errorMessage = `Erro de rede ou servidor: ${axiosError.message}`;
                }
            }
            setError(errorMessage);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        // Layout de duas colunas
        <div className="flex min-h-screen bg-gray-900 text-white">
            {/* Coluna Esquerda */}
            <div className="hidden lg:flex w-1/2 items-center justify-center bg-gradient-to-br from-gray-800 via-gray-900 to-black p-10">
                {/* adicionar logo e mudar o design mais pra frente */}
                <div className="text-center">
                    <h1 className="text-4xl font-bold mb-4">Conecta Pro</h1>
                    <p className="text-xl text-gray-400">Conectando profissionais e clientes.</p>
                </div>
            </div>

            {/* Coluna Direita (Formulário de Registro) */}
            <div className="w-full lg:w-1/2 flex items-center justify-center p-8 md:p-12">
                <div className="w-full max-w-md">
                    <h2 className="text-3xl font-bold mb-6 text-center">Register</h2>

                    <form onSubmit={handleSubmit}>

                        {/* Mensagem de Erro */}
                        {error && (
                            <div className="mb-4 p-3 bg-red-900 border border-red-700 text-red-200 rounded-md text-center">
                                {error}
                            </div>
                        )}

                        {/* Campos específicos de Registro usando componentes reutilizáveis */}
                        <div className="mb-4">
                            <Input id="nome" label="Nome" type="text" value={nome} onChange={(e) => setNome(e.target.value)} placeholder="Seu nome completo" required />
                        </div>
                        <div className="mb-4">
                            <Input id="email" label="Email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="seuemail@exemplo.com" required />
                        </div>
                        <div className="mb-4">
                            <Input id="nomeUsuarioReg" label="Nome de usuário" type="text" value={nomeUsuarioReg} onChange={(e) => setNomeUsuarioReg(e.target.value)} placeholder="Escolha um nome de usuário" required />
                        </div>
                        <div className="mb-4">
                            <Input id="telefone" label="Telefone" type="tel" value={telefone} onChange={(e) => setTelefone(e.target.value)} placeholder="(Opcional) Seu telefone" />
                        </div>
                        <div className="mb-4">
                            <Input id="senhaReg" label="Senha" type="password" value={senhaReg} onChange={(e) => setSenhaReg(e.target.value)} placeholder="Crie uma senha (min. 6 caracteres)" required />
                        </div>
                        <div className="mb-6">
                            <Input id="confirmarSenha" label="Confirmar senha" type="password" value={confirmarSenha} onChange={(e) => setConfirmarSenha(e.target.value)} placeholder="Digite a senha novamente" required />
                        </div>

                        {/* Checkbox Termos */}
                        <div className="mb-6 flex items-center">
                            <input type="checkbox" id="termos" checked={termos} onChange={(e) => setTermos(e.target.checked)} className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-600 rounded bg-gray-800" required />
                            <label htmlFor="termos" className="ml-2 block text-sm text-gray-400">Li e concordo com os <a href="#" className="text-blue-400 hover:text-blue-300">termos de serviço</a></label>
                        </div>

                        {/* Botão Confirmar */}
                        <Button type="submit" isLoading={isLoading}>
                            Confirmar
                        </Button>

                        {/* Divisor e Login Social/Alternar Modo */}
                        <div className="mt-6 text-center">
                            <div className="relative my-4">
                                <div className="absolute inset-0 flex items-center">
                                    <div className="w-full border-t border-gray-700"></div>
                                </div>
                                <div className="relative flex justify-center text-sm">
                                    <span className="px-2 bg-gray-900 text-gray-500">Ou continue com</span>
                                </div>
                            </div>
                            <div className="flex justify-center space-x-4">
                                <button type="button" className="p-2 bg-gray-700 rounded-full hover:bg-gray-600">G</button>
                                <button type="button" className="p-2 bg-gray-700 rounded-full hover:bg-gray-600">F</button>
                                <button type="button" className="p-2 bg-gray-700 rounded-full hover:bg-gray-600">A</button>
                            </div>

                            {/* Link para Login */}
                            <p className="mt-6 text-sm text-gray-400">
                                Já tem uma conta?
                                <Link href="/login" // Link para a página de login
                                      className="ml-1 text-blue-400 hover:text-blue-300 font-medium">
                                    Faça login
                                </Link>
                            </p>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}