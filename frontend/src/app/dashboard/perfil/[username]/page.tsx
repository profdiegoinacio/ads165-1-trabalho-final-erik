export const dynamic = 'force-dynamic';

import { auth } from "@/auth";
import ProfilePageClient from "@/components/perfil/ProfilePageClient";
import { PerfilUsuario, Post, AreaDeAtuacao } from '@/types';
import { Suspense } from "react";
import React from 'react';

async function getDataForProfilePage(username: string, token: string | undefined) {
    const headers = token ? { 'Authorization': `Bearer ${token}` } : {};

    const promises = [
        fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/usuarios/${username}/perfil`, { headers, cache: 'no-store' }),
        fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/postagens/usuario/${username}`, { cache: 'no-store' }),
        fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/areas-de-atuacao`, { headers, cache: 'no-store' })
    ];

    try {
        const [profileRes, postsRes, areasRes] = await Promise.all(promises);

        const profile: PerfilUsuario | null = profileRes.ok ? await profileRes.json() : null;
        const postsContent: { content: Post[] } = postsRes.ok ? await postsRes.json() : { content: [] };
        const todasAsAreas: AreaDeAtuacao[] = areasRes.ok ? await areasRes.json() : [];

        return { initialProfile: profile, initialPosts: postsContent.content || [], todasAsAreas };
    } catch (error) {
        console.error("Falha crítica ao buscar dados para a página de perfil:", error);
        return { initialProfile: null, initialPosts: [], todasAsAreas: [] };
    }
}

export default async function PaginaPerfil({ params }: { params: { username: string } }) {
    const session = await auth();
    const loggedInUsername = session?.user?.name ?? null;

    const { initialProfile, initialPosts, todasAsAreas } = await getDataForProfilePage(params.username, session?.accessToken);

    if (!initialProfile) {
        return <div className="p-10 text-center text-gray-400">Perfil não encontrado ou erro ao carregar.</div>;
    }

    return (
        <Suspense fallback={<div className="p-10 text-center text-gray-400">Carregando perfil...</div>}>
            <ProfilePageClient
                initialProfile={initialProfile}
                initialPosts={initialPosts}
                todasAsAreas={todasAsAreas}
                loggedInUsername={loggedInUsername}
            />
        </Suspense>
    );
}