// src/components/layout/SidebarDireita.tsx
"use client"; // Transforma em um Client Component para podermos usar estado e efeitos

import React, { useState, useEffect } from 'react';
import Link from 'next/link';
import api from '@/lib/api';
import { SearchedUser } from '@/types/user';

export default function SidebarDireita() {
    // Estados do componente
    const [query, setQuery] = useState(''); // O que o usuário digita na busca
    const [results, setResults] = useState<SearchedUser[]>([]); // A lista de resultados da API
    const [isLoading, setIsLoading] = useState(false);

    // Efeito para executar a busca com "debouncing"
    useEffect(() => {
        // Se a query estiver vazia, limpa os resultados e não faz nada
        if (query.trim() === '') {
            setResults([]);
            return;
        }

        setIsLoading(true);

        // O 'debouncing' evita que uma chamada à API seja feita a cada letra digitada.
        // Ele espera o usuário parar de digitar por 300ms antes de fazer a busca.
        const delayDebounceFn = setTimeout(() => {
            api.get('/usuarios/buscar', { params: { query: query, size: 5 } }) // Busca até 5 resultados
                .then(response => {
                    setResults(response.data.content);
                })
                .catch(error => {
                    console.error("Erro ao buscar usuários:", error);
                    setResults([]); // Limpa os resultados em caso de erro
                })
                .finally(() => {
                    setIsLoading(false);
                });
        }, 300); // Atraso de 300 milissegundos

        // Função de limpeza: cancela o timeout anterior se o usuário digitar novamente
        return () => clearTimeout(delayDebounceFn);
    }, [query]); // Este efeito roda toda vez que a 'query' (texto da busca) muda

    return (
        <div className="w-[350px] ml-6 hidden lg:block">
            <div className="h-screen sticky top-0 flex flex-col py-2 space-y-4">
                {/* Barra de Busca */}
                <div className="relative">
                    <input
                        type="text"
                        placeholder="Buscar Profissionais..."
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        className="w-full p-2 pl-10 bg-gray-800 border border-gray-700 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500 text-white"
                    />
                    <svg className="w-5 h-5 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path></svg>
                </div>

                {/* Container de Resultados da Busca */}
                {(query.length > 0 || isLoading) && ( // Mostra o container se houver busca ou estiver carregando
                    <div className="bg-gray-800 rounded-xl">
                        {isLoading && <p className="p-4 text-center text-gray-400">Buscando...</p>}

                        {!isLoading && results.length > 0 && (
                            results.map(user => (
                                <Link
                                    href={`/dashboard/perfil/${user.nomeUsuario}`}
                                    key={user.id}
                                    className="block hover:bg-gray-700 rounded-lg"
                                    onClick={() => setQuery('')} // Limpa a busca ao clicar
                                >
                                    <div className="flex items-center space-x-3 p-3">
                                        <div className="w-10 h-10 bg-gray-600 rounded-full flex-shrink-0"></div>
                                        <div>
                                            <p className="font-semibold text-white">{user.nome}</p>
                                            <p className="text-sm text-gray-400">@{user.nomeUsuario}</p>
                                        </div>
                                    </div>
                                </Link>
                            ))
                        )}

                        {!isLoading && query.length > 0 && results.length === 0 && (
                            <p className="p-4 text-center text-gray-400">Nenhum resultado encontrado.</p>
                        )}
                    </div>
                )}

                {/* Card de Destaques (pode ser movido para baixo ou mantido) */}
                <div className="bg-gray-800 rounded-xl p-4">
                    <h2 className="text-xl font-bold text-white mb-4">Destaques</h2>
                    {/* ... (conteúdo dos destaques) ... */}
                </div>
            </div>
        </div>
    );
}