'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { Search } from 'lucide-react';

export default function SidebarDireita() {

    const [searchTerm, setSearchTerm] = useState('');

    const router = useRouter();

    const handleSearch = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter' && searchTerm.trim() !== '') {
            router.push(`/dashboard/buscar?q=${encodeURIComponent(searchTerm.trim())}`);
        }
    };

    const destaques = [
        { categoria: 'Design', nome: 'Igor', arroba: '@igor', avaliacao: 5, numAvaliacoes: 123 },
        { categoria: 'Desenvolvimento Web', nome: 'Ana', arroba: '@ana_dev', avaliacao: 4.9, numAvaliacoes: 89 },
        { categoria: 'Marketing Digital', nome: 'Carlos', arroba: '@carlosmkt', avaliacao: 4.8, numAvaliacoes: 204 },
    ];

    return (
        <aside className="w-80 p-4 space-y-6">
            <div className="relative">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500" size={20} />
                <input
                    type="text"
                    placeholder="Buscar..."
                    className="w-full bg-gray-800 border border-gray-700 rounded-full pl-10 pr-4 py-2 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    onKeyDown={handleSearch}
                />
            </div>

            <div className="bg-gray-900 rounded-2xl p-4">
                <h2 className="text-xl font-bold text-white mb-4">Destaques</h2>
                <div className="space-y-4">
                    {destaques.map((destaque, index) => (
                        <div key={index}>
                            <p className="text-sm text-gray-400">{destaque.categoria}</p>
                            <div className="flex items-center justify-between mt-1">
                                <div>
                                    <p className="font-bold text-white">{destaque.nome}</p>
                                    <p className="text-sm text-gray-500">{destaque.arroba}</p>
                                </div>
                                <button className="px-4 py-1 text-sm font-semibold bg-white text-black rounded-full hover:bg-gray-200">
                                    Ver
                                </button>
                            </div>
                            <p className="text-xs text-gray-400 mt-1">
                                Avaliação: {destaque.avaliacao.toFixed(1)} ({destaque.numAvaliacoes} reviews)
                            </p>
                        </div>
                    ))}
                </div>
            </div>
        </aside>
    );
}
