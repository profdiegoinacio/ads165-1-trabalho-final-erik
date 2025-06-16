"use client";

import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from 'next/navigation';
import Image from 'next/image';
import { useSession } from 'next-auth/react';
import api from '@/lib/api';

// Tipos
import { PerfilUsuario } from '@/types/perfil';
import { Post } from '@/types/post';

// Componentes
import ModalEditarPerfil from '@/components/perfil/ModalEditarPerfil';
import ModalDeixarAvaliacao from '@/components/perfil/ModalDeixarAvaliacao';
import Postagem from '@/components/dashboard/Postagem';
import StarRating from '@/components/ui/StarRating';

// Interface para a resposta paginada da API de posts
interface PaginatedPosts {
    content: Post[];
}

export default function PaginaPerfil() {
    // --- Hooks ---
    const params = useParams();
    const { data: session, status: sessionStatus } = useSession(); // Pegamos o status da sessão também

    // --- Estados do Componente ---
    const [perfil, setPerfil] = useState<PerfilUsuario | null>(null);
    const [userPosts, setUserPosts] = useState<Post[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Estados para controlar os modais
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [isReviewModalOpen, setIsReviewModalOpen] = useState(false);

    // --- Variáveis Derivadas ---
    const username = params.username as string;
    const isOwnProfile = session?.user?.username === username;

    // --- Funções ---
    // Função para buscar todos os dados do perfil (infos + posts)
    const fetchAllProfileData = useCallback(async () => {
        if (!username) return;
        setIsLoading(true);
        setError(null);
        try {
            const [perfilResponse, postsResponse] = await Promise.all([
                api.get<PerfilUsuario>(`/usuarios/${username}/perfil`),
                api.get<PaginatedPosts>(`/postagens/usuario/${username}`)
            ]);
            setPerfil(perfilResponse.data);
            setUserPosts(postsResponse.data.content);
        } catch (err) {
            console.error("Erro ao buscar dados do perfil:", err);
            setError("Perfil não encontrado ou erro ao carregar.");
        } finally {
            setIsLoading(false);
        }
    }, [username]);

    // Efeito para buscar os dados quando o componente montar ou o username mudar
    useEffect(() => {
        fetchAllProfileData();
    }, [fetchAllProfileData]);

    // Callback para ser chamado após uma atualização bem-sucedida do perfil
    const handleProfileUpdate = () => {
        setIsEditModalOpen(false); // Fecha o modal de edição
        fetchAllProfileData(); // Recarrega todos os dados do perfil
    };

    // Callback para ser chamado após uma nova avaliação ser enviada
    const handleReviewSubmitted = () => {
        setIsReviewModalOpen(false); // Fecha o modal de avaliação
        fetchAllProfileData(); // Recarrega todos os dados do perfil (para atualizar a nota média)
    };

    // --- Lógica de Renderização ---
    if (isLoading || sessionStatus === 'loading') {
        return <div className="p-10 text-center text-gray-400">Carregando perfil...</div>;
    }
    if (error) {
        return <div className="p-10 text-center text-red-500">{error}</div>;
    }
    if (!perfil) {
        return <div className="p-10 text-center text-gray-400">Perfil não encontrado.</div>;
    }

    return (
        <>
            <div>
                {/* Header da Página */}
                <div className="p-4 border-b border-gray-800 sticky top-0 bg-black/70 backdrop-blur-md">
                    <h1 className="text-xl font-bold text-white">{perfil.nome}</h1>
                    <p className="text-sm text-gray-500">{userPosts.length} postagens</p>
                </div>

                <div>
                    {/* Capa */}
                    <div className="h-48 md:h-64 bg-gray-700 relative">
                        {perfil.fotoCapaUrl && <Image src={perfil.fotoCapaUrl} alt="Foto de capa" layout="fill" objectFit="cover" priority />}
                    </div>
                    <div className="p-4">
                        <div className="flex justify-between items-start -mt-20">
                            {/* Avatar */}
                            <div className="w-28 h-28 md:w-36 md:h-36 bg-gray-800 rounded-full border-4 border-black relative overflow-hidden">
                                {perfil.fotoPerfilUrl ? <Image src={perfil.fotoPerfilUrl} alt="Foto de perfil" layout="fill" objectFit="cover" /> : <div className="w-full h-full bg-gray-600"></div>}
                            </div>
                            {/* Botões de Ação */}
                            <div className="pt-20 md:pt-24">
                                {isOwnProfile ? (
                                    <button onClick={() => setIsEditModalOpen(true)} className="px-4 py-2 text-sm font-semibold border border-white rounded-full hover:bg-gray-800">Editar Perfil</button>
                                ) : (
                                    <div className="flex space-x-2">
                                        <button onClick={() => setIsReviewModalOpen(true)} className="px-4 py-2 text-sm font-semibold border border-white rounded-full hover:bg-gray-800">Avaliar</button>
                                        <button className="px-4 py-2 text-sm font-semibold bg-white text-black rounded-full hover:bg-gray-200">Seguir</button>
                                    </div>
                                )}
                            </div>
                        </div>
                        {/* Informações */}
                        <div className="mt-4">
                            <h2 className="text-xl font-bold">{perfil.nome}</h2>
                            <p className="text-sm text-gray-400">@{perfil.nomeUsuario}</p>
                            <div className="flex items-center space-x-2 mt-3">
                                <StarRating rating={perfil.notaMedia || 0} />
                                <span className="text-sm text-gray-400">
                  {perfil.notaMedia?.toFixed(1)} ({perfil.totalAvaliacoes} avaliações)
                </span>
                            </div>
                            {perfil.formacao && <p className="mt-3 text-white font-semibold">{perfil.formacao}</p>}
                            <p className="mt-2 text-gray-300 whitespace-pre-wrap">{perfil.bio || "Nenhuma biografia disponível."}</p>
                        </div>
                    </div>
                    {/* Abas */}
                    <div className="border-b border-gray-700 mt-4">
                        <nav className="flex" aria-label="Tabs"><a href="#" className="px-3 py-2 font-medium text-sm rounded-t-lg border-b-2 border-blue-500 text-blue-500">Publicações</a></nav>
                    </div>
                    {/* Conteúdo da Aba */}
                    <div>
                        {userPosts.length > 0 ? (
                            userPosts.map(post => <Postagem key={post.id} post={post} />)
                        ) : (
                            <p className="p-8 text-center text-gray-500">Este usuário ainda não fez publicações.</p>
                        )}
                    </div>
                </div>
            </div>

            {/* Renderização dos Modais */}
            <ModalEditarPerfil
                isOpen={isEditModalOpen}
                onClose={() => setIsEditModalOpen(false)}
                profileData={perfil}
                onProfileUpdate={handleProfileUpdate}
            />
            <ModalDeixarAvaliacao
                isOpen={isReviewModalOpen}
                onClose={() => setIsReviewModalOpen(false)}
                profileOwnerUsername={perfil.nomeUsuario}
                onReviewSubmitted={handleReviewSubmitted}
            />
        </>
    );
}