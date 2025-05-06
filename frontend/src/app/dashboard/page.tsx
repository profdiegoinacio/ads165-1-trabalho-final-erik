"use client";

import React from 'react';
import { useRouter } from 'next/navigation';
import api from '@/services/axiosConfig';

export default function PaginaDashboard() {
    const router = useRouter();

    //logout
    const handleLogout = () => {
        localStorage.removeItem('authToken'); // Remove o token

        // Limpa o header de autorização padrão do axios (se configurado)
        // Isso evita que requisições futuras tentem usar o token antigo
        if (api.defaults.headers.common['Authorization']) {
            delete api.defaults.headers.common['Authorization'];
        }

        alert("Você foi desconectado."); // desabilitar quando a pagina estiver estruturada

        // Redireciona para o login usando o router
        router.push('/login');
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-900 text-white p-4">
            <div className="text-center">
                <h1 className="text-6xl mb-4">🚧</h1>
                <h2 className="text-3xl font-bold mb-2">Página em Construção</h2>
                <p className="text-xl text-gray-400 mb-8">(Work in Progress)</p>
                <p className="text-gray-500 mb-4">
                    Login realizado com sucesso! Esta será sua área principal.
                </p>

                {/* Botão de Logout */}
                <button
                    onClick={handleLogout}
                    className="bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-6 rounded-md transition duration-300 ease-in-out"
                >
                    Logout (Sair)
                </button>

            </div>
        </div>
    );
}