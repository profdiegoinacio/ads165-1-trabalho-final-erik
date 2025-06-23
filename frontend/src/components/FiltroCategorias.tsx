"use client";

import { useState, useEffect } from 'react';
import Link from 'next/link';
import Image from 'next/image';
import api from '@/lib/api';
import { AreaDeAtuacao } from '@/types';

interface UsuarioListado {
    id: number;
    nome: string;
    nomeUsuario: string;
    fotoPerfilUrl: string | null;
    notaMedia?: number | null;
}

interface FiltroProps {
    areasIniciais: AreaDeAtuacao[];
}

export default function FiltroCategorias({ areasIniciais }: FiltroProps) {
    const [selectedAreaId, setSelectedAreaId] = useState<number | null>(null);
    const [usuarios, setUsuarios] = useState<UsuarioListado[]>([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        if (selectedAreaId === null) {
            setUsuarios([]);
            return;
        }

        const fetchUsuarios = async () => {
            setIsLoading(true);
            try {
                const response = await api.get('/usuarios/buscar', {
                    params: { areaId: selectedAreaId, size: 21 }
                });
                setUsuarios(response.data.content);
            } catch (error) {
                console.error("Erro ao buscar usuários:", error);
                setUsuarios([]);
            } finally {
                setIsLoading(false);
            }
        };

        fetchUsuarios();
    }, [selectedAreaId]);

    return (
        <div>
            <div className="mb-8">
                <h2 className="text-xl font-semibold mb-4">Selecione uma Área</h2>
                <div className="flex flex-wrap gap-3">
                    {areasIniciais.map((area) => (
                        <button
                            key={area.id}
                            onClick={() => setSelectedAreaId(area.id === selectedAreaId ? null : area.id)}
                            className={`px-4 py-2 text-sm font-medium rounded-full transition-all duration-200 ease-in-out
                                ${area.id === selectedAreaId
                                ? 'bg-blue-600 text-white shadow-lg scale-105'
                                : 'bg-gray-700 text-gray-300 hover:bg-gray-600'
                            }
                            `}
                        >
                            {area.nome}
                        </button>
                    ))}
                </div>
            </div>

            <div>
                <h2 className="text-xl font-semibold mb-4">Profissionais Encontrados</h2>
                {isLoading ? (
                    <div className="text-center text-gray-400">Buscando profissionais...</div>
                ) : (
                    usuarios.length > 0 ? (
                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                            {usuarios.map(usuario => (
                                <div key={usuario.id} className="bg-gray-800 rounded-lg p-4 flex flex-col items-center text-center hover:bg-gray-700/50 transition-colors duration-300">
                                    <div className="w-24 h-24 relative mb-4">
                                        <Image
                                            src={usuario.fotoPerfilUrl || '/default-avatar.png'}
                                            alt={`Foto de ${usuario.nome}`}
                                            layout="fill"
                                            objectFit="cover"
                                            className="rounded-full"
                                        />
                                    </div>
                                    <h3 className="font-bold text-lg text-white">{usuario.nome}</h3>
                                    <p className="text-sm text-gray-400">@{usuario.nomeUsuario}</p>

                                    {usuario.notaMedia && (
                                        <p className="text-xs text-yellow-400 mt-1">
                                            Avaliação: {usuario.notaMedia.toFixed(1)} ★
                                        </p>
                                    )}

                                    <Link href={`/dashboard/perfil/${usuario.nomeUsuario}`} legacyBehavior>
                                        <a className="mt-4 w-full bg-blue-600 text-white py-2 rounded-lg text-sm font-semibold hover:bg-blue-700 transition-colors">
                                            Ver Perfil
                                        </a>
                                    </Link>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="text-center text-gray-500 py-8">
                            <p>{selectedAreaId ? 'Nenhum profissional encontrado nesta área.' : 'Selecione uma área acima para começar.'}</p>
                        </div>
                    )
                )}
            </div>
        </div>
    );
}