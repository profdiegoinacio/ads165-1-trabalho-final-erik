'use client';

import React, { useState, useEffect, Suspense } from 'react';
import { useSearchParams } from 'next/navigation';
import Link from 'next/link';
import Image from 'next/image';
import api from '@/lib/api';
import { UsuarioResponseDTO } from '@/types/user';
import { Search, User as UserIcon } from 'lucide-react';

interface PaginatedUsers {
    content: UsuarioResponseDTO[];
}

function UserCard({ user }: { user: UsuarioResponseDTO }) {
    return (
        <Link href={`/dashboard/perfil/${user.nomeUsuario}`} passHref>
            <div className="flex items-center p-4 space-x-4 hover:bg-gray-900 transition-colors cursor-pointer border-b border-gray-800">
                <div className="w-12 h-12 bg-gray-700 rounded-full relative overflow-hidden flex-shrink-0">

                    {user.perfil?.fotoPerfilUrl ? (
                        <Image src={user.perfil.fotoPerfilUrl} alt={`Foto de ${user.nome}`} layout="fill" objectFit="cover" />
                    ) : (
                        <div className="w-full h-full bg-gray-800 flex items-center justify-center">
                            <UserIcon size={24} className="text-gray-500" />
                        </div>
                    )}
                </div>
                <div className="flex-1">
                    <p className="font-bold text-white hover:underline">{user.nome}</p>
                    <p className="text-sm text-gray-400">@{user.nomeUsuario}</p>
                    <p className="text-sm text-gray-300 mt-1 truncate">{user.perfil?.bio || 'Sem biografia.'}</p>
                </div>
            </div>
        </Link>
    );
}

function SearchResults() {
    const searchParams = useSearchParams();
    const query = searchParams.get('q');

    const [results, setResults] = useState<UsuarioResponseDTO[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (query) {
            const fetchResults = async () => {
                setIsLoading(true);
                setError(null);
                try {
                    const response = await api.get<PaginatedUsers>('/usuarios/buscar', {
                        params: { query: query, page: 0, size: 20 }
                    });
                    setResults(response.data.content);
                } catch (err) {
                    console.error("Erro ao buscar resultados:", err);
                    setError("Falha ao carregar os resultados da busca.");
                } finally {
                    setIsLoading(false);
                }
            };
            fetchResults();
        } else {
            setResults([]);
            setIsLoading(false);
        }
    }, [query]);

    return (
        <div className="text-white">
            <div className="p-4 border-b border-gray-800 sticky top-0 bg-black/70 backdrop-blur-md">
                <h1 className="text-xl font-bold">Resultados da Busca</h1>
                {query && <p className="text-sm text-gray-500">Mostrando resultados para: "{query}"</p>}
            </div>

            {isLoading && <p className="p-8 text-center text-gray-400">Buscando...</p>}
            {error && <p className="p-8 text-center text-red-500">{error}</p>}

            {!isLoading && !error && (
                <div className="flex flex-col">
                    {results.length > 0 ? (
                        results.map(user => <UserCard key={user.id} user={user} />)
                    ) : (
                        <div className="p-8 text-center text-gray-500">
                            <Search size={48} className="mx-auto mb-4" />
                            <p>Nenhum resultado encontrado para "{query}".</p>
                            <p className="text-xs mt-2">Tente buscar por outro nome ou termo.</p>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

export default function PaginaBusca() {
    return (
        <Suspense fallback={<div className="p-10 text-center text-gray-400">Carregando busca...</div>}>
            <SearchResults />
        </Suspense>
    );
}
