"use client";

import React, { useState, useCallback } from 'react';
import Image from 'next/image';
import api from '@/lib/api';
import { PerfilUsuario, Post, AreaDeAtuacao } from '@/types';
import ModalEditarPerfil from '@/components/perfil/ModalEditarPerfil';
import ModalDeixarAvaliacao from '@/components/perfil/ModalDeixarAvaliacao';
import Postagem from '@/components/dashboard/Postagem';
import StarRating from '@/components/ui/StarRating';
import { ImageIcon, User as UserIcon } from 'lucide-react';

interface ProfileClientProps {
    initialProfile: PerfilUsuario;
    initialPosts: Post[];
    todasAsAreas: AreaDeAtuacao[];
    loggedInUsername: string | null;
}

export default function ProfilePageClient({ initialProfile, initialPosts, todasAsAreas, loggedInUsername }: ProfileClientProps) {
    console.log("DADOS DO PERFIL RECEBIDOS NO CLIENTE:", initialProfile);

    const [perfil, setPerfil] = useState<PerfilUsuario>(initialProfile);
    const [userPosts, setUserPosts] = useState<Post[]>(initialPosts);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [isReviewModalOpen, setIsReviewModalOpen] = useState(false);
    const [isFollowing, setIsFollowing] = useState(perfil.seguindoPeloUsuarioLogado);
    const [isFollowLoading, setIsFollowLoading] = useState(false);

    const isOwnProfile = loggedInUsername === perfil.nomeUsuario;

    const refreshProfileData = useCallback(async () => {
        try {
            const perfilResponse = await api.get<PerfilUsuario>(`/usuarios/${perfil.nomeUsuario}/perfil`);
            setPerfil(perfilResponse.data);
            setIsFollowing(perfilResponse.data.seguindoPeloUsuarioLogado);
        } catch (err) {
            console.error("Erro ao recarregar dados do perfil:", err);
        }
    }, [perfil.nomeUsuario]);

    const onActionSuccess = () => {
        setIsEditModalOpen(false);
        setIsReviewModalOpen(false);
        refreshProfileData();
    };

    const handleFollowToggle = async () => {
        if (!loggedInUsername) return alert("Você precisa estar logado para interagir.");
        setIsFollowLoading(true);
        const originalFollowState = isFollowing;

        setPerfil(p => ({ ...p, totalSeguidores: originalFollowState ? p.totalSeguidores - 1 : p.totalSeguidores + 1 }));
        setIsFollowing(!originalFollowState);
        try {
            await api.post(`/usuarios/${perfil.nomeUsuario}/${originalFollowState ? 'deixar-de-seguir' : 'seguir'}`);
        } catch (error) {
            console.error("Falha na ação de seguir/deixar de seguir:", error);
            alert("Ocorreu um erro. Tente novamente.");

            refreshProfileData();
        } finally {
            setIsFollowLoading(false);
        }
    };

    return (
        <div className="text-white">
            {/* Header */}
            <div className="p-4 border-b border-gray-800 sticky top-0 bg-black/80 backdrop-blur-md z-10">
                <h1 className="text-xl font-bold text-white">{perfil.nome}</h1>
                <p className="text-sm text-gray-500">{userPosts.length} postagens</p>
            </div>

            <div>
                <div className="h-48 md:h-64 bg-gray-800/50 relative flex items-center justify-center">
                    {/* CORREÇÃO 1: Lógica de fallback para a imagem de capa */}
                    {perfil.fotoCapaUrl ? (
                        <Image src={perfil.fotoCapaUrl} alt="Foto de capa" layout="fill" objectFit="cover" priority unoptimized/>
                    ) : (
                        <ImageIcon className="h-12 w-12 text-gray-600" />
                    )}
                </div>

                <div className="p-4">
                    <div className="flex justify-between items-start">
                        <div className="w-28 h-28 md:w-36 md:h-36 bg-gray-900 rounded-full -mt-20 border-4 border-black relative overflow-hidden flex items-center justify-center">
                            <Image
                                src={perfil.fotoPerfilUrl || '/default-avatar.png'}
                                alt="Foto de perfil"
                                layout="fill"
                                objectFit="cover"
                                className="rounded-full"
                                unoptimized
                            />
                        </div>
                        <div className="pt-4">
                            {isOwnProfile ? (
                                <button onClick={() => setIsEditModalOpen(true)} className="px-4 py-2 text-sm font-semibold border border-gray-600 rounded-full hover:bg-gray-800 transition-colors">Editar Perfil</button>
                            ) : (
                                <div className="flex space-x-2">
                                    <button onClick={() => setIsReviewModalOpen(true)} className="px-4 py-2 text-sm font-semibold border border-gray-600 rounded-full hover:bg-gray-800 transition-colors">Avaliar</button>
                                    <button
                                        onClick={handleFollowToggle}
                                        disabled={isFollowLoading || !loggedInUsername}
                                        className={`px-4 py-2 text-sm font-semibold rounded-full transition-colors duration-200 w-28 text-center flex items-center justify-center ${
                                            isFollowLoading ? 'bg-gray-500 cursor-not-allowed' : isFollowing ? 'bg-transparent border border-gray-600 hover:bg-red-800/20 hover:border-red-700 hover:text-red-500' : 'bg-white text-black hover:bg-gray-200'
                                        }`}
                                    >
                                        {isFollowLoading ? <div className="h-5 w-5 border-2 border-t-transparent border-white rounded-full animate-spin"></div> : (isFollowing ? 'Seguindo' : 'Seguir')}
                                    </button>
                                </div>
                            )}
                        </div>
                    </div>

                    <div className="mt-4 space-y-2">
                        <div className="flex items-center space-x-2">
                            <h2 className="text-2xl font-extrabold text-white">{perfil.nome}</h2>
                            {perfil.isProfissional && <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-5 h-5 text-blue-500"><path fillRule="evenodd" d="M8.603 3.799A4.49 4.49 0 0112 2.25c1.357 0 2.573.6 3.397 1.549a4.49 4.49 0 013.498 1.307 4.491 4.491 0 011.307 3.497A4.49 4.49 0 0121.75 12a4.49 4.49 0 01-1.549 3.397 4.491 4.491 0 01-1.307 3.497 4.491 4.491 0 01-3.497 1.307A4.49 4.49 0 0112 21.75a4.49 4.49 0 01-3.397-1.549 4.49 4.49 0 01-3.498-1.306 4.491 4.491 0 01-1.307-3.498A4.49 4.49 0 012.25 12c0-1.357.6-2.573 1.549-3.397a4.49 4.49 0 011.307-3.497 4.49 4.49 0 013.497-1.307zm7.007 6.387a.75.75 0 10-1.22-.872l-3.236 4.53L9.53 12.22a.75.75 0 00-1.06 1.06l2.25 2.25a.75.75 0 001.14-.094l3.75-5.25z" clipRule="evenodd" /></svg>}
                        </div>
                        <p className="text-md text-gray-500">@{perfil.nomeUsuario}</p>
                        {perfil.formacao && <p className="text-base text-gray-300">{perfil.formacao}</p>}
                        <p className="text-base text-white whitespace-pre-wrap pt-2">{perfil.bio || "Nenhuma biografia disponível."}</p>
                        <div className="flex items-center space-x-2 pt-2">
                            <StarRating rating={perfil.notaMedia || 0} />
                            <span className="text-sm text-gray-500">{perfil.notaMedia?.toFixed(1)} ({perfil.totalAvaliacoes} avaliações)</span>
                        </div>
                        <div className="flex space-x-4 text-sm text-gray-500 pt-2">
                            <p><span className="font-bold text-white">{perfil.totalSeguindo}</span> Seguindo</p>
                            <p><span className="font-bold text-white">{perfil.totalSeguidores}</span> Seguidores</p>
                        </div>
                    </div>
                </div>

                <div className="border-b border-gray-700 mt-4">
                    <nav className="flex px-4"><a href="#" className="px-3 py-2 font-medium text-sm border-b-2 border-blue-500 text-blue-500">Publicações</a></nav>
                </div>
                <div>
                    {userPosts.length > 0 ?
                        userPosts.map(post => <Postagem key={post.id} post={post} />) :
                        <p className="p-8 text-center text-gray-500">Este usuário ainda não fez publicações.</p>
                    }
                </div>
            </div>

            <ModalEditarPerfil isOpen={isEditModalOpen} onClose={() => setIsEditModalOpen(false)} profileData={perfil} onProfileUpdate={onActionSuccess} todasAsAreas={todasAsAreas} />
            <ModalDeixarAvaliacao isOpen={isReviewModalOpen} onClose={() => setIsReviewModalOpen(false)} profileOwnerUsername={perfil.nomeUsuario} onReviewSubmitted={onActionSuccess} />
        </div>
    );
}