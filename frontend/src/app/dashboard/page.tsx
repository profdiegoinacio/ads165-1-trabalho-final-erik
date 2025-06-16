// src/app/dashboard/page.tsx
"use client";

import React, { useState, useEffect, useCallback } from 'react';
import { useRouter } from 'next/navigation';
import { signOut, useSession } from 'next-auth/react';
import FormularioCriarPostagem from '@/components/dashboard/FormularioCriarPostagem';
import Postagem from '@/components/dashboard/Postagem';
import api from '@/lib/api';
import { Post } from '@/types/post';
import DebugAuthStatus from '@/components/DebugAuthStatus';

// Interface para a resposta paginada da nossa API
interface PaginatedPosts {
    content: Post[];
}

export default function PaginaDashboard() {
    const router = useRouter();
    const { status } = useSession();

    const [postagens, setPostagens] = useState<Post[]>([]);
    const [isLoadingPosts, setIsLoadingPosts] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Criamos uma função para buscar os posts que pode ser reutilizada.
    // Usamos useCallback para que a função não seja recriada em cada renderização,
    // a menos que suas dependências mudem (neste caso, nenhuma).
    const fetchPosts = useCallback(async () => {
        console.log("Buscando postagens...");
        setIsLoadingPosts(true);
        setError(null);
        try {
            const response = await api.get<PaginatedPosts>('/postagens');
            setPostagens(response.data.content);
        } catch (err) {
            console.error("Falha ao buscar postagens:", err);
            setError("Não foi possível carregar o feed.");
        } finally {
            setIsLoadingPosts(false);
        }
    }, []);

    // useEffect para proteger a rota e buscar os dados iniciais
    useEffect(() => {
        if (status === 'unauthenticated') {
            router.replace('/login');
        } else if (status === 'authenticated') {
            fetchPosts();
        }
    }, [status, router, fetchPosts]);

    const handleLogout = async () => {
        await signOut({ redirect: true, callbackUrl: '/login' });
    };

    // Se a sessão está carregando, mostramos um loader.
    if (status === 'loading') {
        return (
            <div className="flex items-center justify-center min-h-screen bg-gray-900 text-white">
                <p>Verificando sessão...</p>
            </div>
        );
    }

    // Se o usuário está autenticado, mostramos o dashboard.
    if (status === 'authenticated') {
        return (
            <div>
                <div className="p-4 border-b border-gray-700 sticky top-0 bg-black/70 backdrop-blur-md flex justify-between items-center">
                    <h1 className="text-xl font-bold text-white">Página Inicial</h1>
                    <button
                        onClick={handleLogout}
                        className="bg-red-600 hover:bg-red-700 text-white font-bold text-sm py-1 px-3 rounded-md transition duration-300"
                    >
                        Logout
                    </button>
                </div>

                {/* Passamos a função fetchPosts para o formulário como prop 'onPostCreated' */}
                <FormularioCriarPostagem onPostCreated={fetchPosts} />

                <section>
                    {error && <p className="text-center text-red-500 p-4">{error}</p>}
                    {isLoadingPosts ? (
                        <p className="text-center text-gray-500 p-8">Carregando postagens...</p>
                    ) : postagens.length > 0 ? (
                        postagens.map((post) => (
                            <Postagem key={post.id} post={post} />
                        ))
                    ) : (
                        <p className="text-center text-gray-500 p-8">Nenhuma postagem encontrada.</p>
                    )}
                </section>

                {/* Mantenha o debug por enquanto para garantir que tudo está ok */}
                <DebugAuthStatus />
            </div>
        );
    }

    // Fallback enquanto redireciona
    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-900 text-white">
            <p>Redirecionando...</p>
        </div>
    );
}