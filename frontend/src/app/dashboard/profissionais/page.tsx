'use client';

import React, { useState, useEffect } from 'react';
import Link from 'next/link';
import Image from 'next/image';
import api from '@/lib/api';
import { UsuarioResponseDTO } from '@/types/user';
import { Briefcase } from 'lucide-react';

interface PaginatedUsers {
    content: UsuarioResponseDTO[];
}

function ProfissionalCard({ user }: { user: UsuarioResponseDTO }) {
    return (
        <Link href={`/dashboard/perfil/${user.nomeUsuario}`} passHref>
            <div className="flex items-center p-4 space-x-4 hover:bg-gray-900 transition-colors cursor-pointer border-b border-gray-800">
                <div className="w-12 h-12 bg-gray-700 rounded-full relative overflow-hidden flex-shrink-0">
                    {user.perfil?.fotoPerfilUrl ? (
                        <Image src={user.perfil.fotoPerfilUrl} alt={`Foto de ${user.nome}`} layout="fill" objectFit="cover" />
                    ) : (
                        <div className="w-full h-full bg-gray-600 flex items-center justify-center">
                            <Briefcase size={24} className="text-gray-400" />
                        </div>
                    )}
                </div>
                <div className="flex-1">
                    <div className="flex items-center space-x-2">
                        <p className="font-bold text-white hover:underline">{user.nome}</p>
                        {user.perfil?.isProfissional && (
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-4 h-4 text-blue-500">
                                <path fillRule="evenodd" d="M8.603 3.799A4.49 4.49 0 0112 2.25c1.357 0 2.573.6 3.397 1.549a4.49 4.49 0 013.498 1.307 4.491 4.491 0 011.307 3.497A4.49 4.49 0 0121.75 12a4.49 4.49 0 01-1.549 3.397 4.491 4.491 0 01-1.307 3.497 4.491 4.491 0 01-3.497 1.307A4.49 4.49 0 0112 21.75a4.49 4.49 0 01-3.397-1.549 4.49 4.49 0 01-3.498-1.306 4.491 4.491 0 01-1.307-3.498A4.49 4.49 0 012.25 12c0-1.357.6-2.573 1.549-3.397a4.49 4.49 0 011.307-3.497 4.49 4.49 0 013.497-1.307zm7.007 6.387a.75.75 0 10-1.22-.872l-3.236 4.53L9.53 12.22a.75.75 0 00-1.06 1.06l2.25 2.25a.75.75 0 001.14-.094l3.75-5.25z" clipRule="evenodd" />
                            </svg>
                        )}
                    </div>
                    <p className="text-sm text-gray-400">@{user.nomeUsuario}</p>
                    <p className="text-sm text-gray-300 mt-1 truncate">{user.perfil?.formacao || 'Profissional da plataforma'}</p>
                </div>
            </div>
        </Link>
    );
}

export default function PaginaProfissionais() {
    const [profissionais, setProfissionais] = useState<UsuarioResponseDTO[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchProfissionais = async () => {
            setIsLoading(true);
            setError(null);
            try {
                const response = await api.get<PaginatedUsers>('/usuarios/profissionais');
                setProfissionais(response.data.content);
            } catch (err) {
                console.error("Erro ao buscar profissionais:", err);
                setError("Não foi possível carregar a lista de profissionais.");
            } finally {
                setIsLoading(false);
            }
        };

        fetchProfissionais();
    }, []);

    return (
        <div className="text-white">
            <div className="p-4 border-b border-gray-800 sticky top-0 bg-black/70 backdrop-blur-md">
                <h1 className="text-xl font-bold">Profissionais</h1>
                <p className="text-sm text-gray-500">Encontre os melhores talentos da plataforma.</p>
            </div>

            {isLoading && <p className="p-8 text-center text-gray-400">Carregando...</p>}
            {error && <p className="p-8 text-center text-red-500">{error}</p>}

            {!isLoading && !error && (
                <div className="flex flex-col">
                    {profissionais.length > 0 ? (
                        profissionais.map(user => <ProfissionalCard key={user.id} user={user} />)
                    ) : (
                        <p className="p-8 text-center text-gray-500">Nenhum profissional encontrado no momento.</p>
                    )}
                </div>
            )}
        </div>
    );
}
