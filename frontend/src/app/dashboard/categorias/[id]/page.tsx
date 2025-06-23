// src/app/dashboard/categorias/[id]/page.tsx
import React from 'react';
import api from '@/lib/api';
import { SearchedUser } from '@/types/user';
import UserCard from '@/components/user/UserCard';

// Tipagem para a resposta da API de busca
interface PaginatedUsers {
    content: SearchedUser[];
}

// Props que a página recebe do Next.js: params da URL e searchParams
interface CategoriaResultadosProps {
    params: { id: string };
    searchParams: { nome?: string };
}

// Função para buscar os profissionais da categoria
async function getProfissionaisPorCategoria(id: string): Promise<SearchedUser[]> {
    try {
        const response = await api.get<PaginatedUsers>('/usuarios/buscar', {
            params: {
                areaId: id,
                size: 50 // Busca até 50 profissionais
            }
        });
        return response.data.content;
    } catch (error) {
        console.error(`Falha ao buscar profissionais da categoria ${id}:`, error);
        return [];
    }
}

export default async function CategoriaResultadosPage({ params, searchParams }: CategoriaResultadosProps) {
    const profissionais = await getProfissionaisPorCategoria(params.id);
    const nomeCategoria = searchParams.nome ? decodeURIComponent(searchParams.nome) : 'Categoria';

    return (
        <div>
            {/* Cabeçalho da Página */}
            <div className="p-4 border-b border-gray-800 sticky top-0 bg-black/70 backdrop-blur-md">
                <h1 className="text-xl font-bold text-white">Profissionais em: {nomeCategoria}</h1>
            </div>

            {/* Grid de Profissionais */}
            <div className="p-4">
                {profissionais.length > 0 ? (
                    <div className="grid grid-cols-1 gap-4">
                        {profissionais.map((prof) => (
                            <UserCard key={prof.id} user={prof} />
                        ))}
                    </div>
                ) : (
                    <p className="text-gray-500 text-center">Nenhum profissional encontrado nesta categoria.</p>
                )}
            </div>
        </div>
    );
}