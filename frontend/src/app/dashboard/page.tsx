// src/app/dashboard/page.tsx
"use client";

import React, { useState, useEffect, useCallback } from 'react';
import { useRouter } from 'next/navigation';
import { signOut, useSession } from 'next-auth/react';
import FormularioCriarPostagem from '@/components/dashboard/FormularioCriarPostagem';
import Postagem from '@/components/dashboard/Postagem';
import api from '@/lib/api';
import { Post } from '@/types/post';

interface PaginatedPosts {
    content: Post[];
}

export default function PaginaDashboard() {
    const router = useRouter();
    const { status } = useSession();

    const [postagens, setPostagens] = useState<Post[]>([]);
    const [isLoadingPosts, setIsLoadingPosts] = useState(true);
    const [error, setError] = useState<string | null>(null);

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

    if (status === 'loading') {
        return (
            <div className="flex items-center justify-center min-h-screen bg-gray-900 text-white">
                <p>Verificando sessão...</p>
            </div>
        );
    }

    if (status === 'authenticated') {
        return (
            <div>
                <div className="p-4 border-b border-gray-700 sticky top-0 bg-black/70 backdrop-blur-md flex justify-between items-center">
                    <h1 className="text-xl font-bold text-white">Página Inicial</h1>

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

            </div>
        );
    }

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-900 text-white">
            <p>Redirecionando...</p>
        </div>
    );
}