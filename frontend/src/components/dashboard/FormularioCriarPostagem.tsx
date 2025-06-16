// src/components/dashboard/FormularioCriarPostagem.tsx
"use client";

import React, { useState, FormEvent } from 'react';
import { useSession } from 'next-auth/react';
import api from '@/lib/api';
import Button from '../ui/Button';

// Definimos a interface para as props que o componente espera receber
interface FormularioProps {
    onPostCreated: () => void; // Espera uma função de callback
}

export default function FormularioCriarPostagem({ onPostCreated }: FormularioProps) {
    const [conteudo, setConteudo] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const { data: session, status } = useSession();

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault();
        if (status !== 'authenticated' || !conteudo.trim() || isLoading) return;

        setIsLoading(true);
        setError(null);

        const token = session.accessToken;

        if (!token) {
            setError("Você precisa estar logado para postar.");
            setIsLoading(false);
            return;
        }

        try {
            await api.post('/postagens',
                { conteudo },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            setConteudo(''); // Limpa o formulário
            onPostCreated(); // <<< CHAMA A FUNÇÃO DO PAI PARA ATUALIZAR A LISTA

        } catch (err) {
            console.error("Erro ao criar postagem:", err);
            setError("Não foi possível criar a postagem. Tente novamente.");
        } finally {
            setIsLoading(false);
        }
    };

    if (status !== 'authenticated') {
        return null; // Não mostra o formulário se o usuário não estiver logado
    }

    return (
        <div className="p-4 border-b border-gray-700">
            <form onSubmit={handleSubmit} className="flex space-x-4">
                <div className="w-12 h-12 bg-gray-600 rounded-full flex-shrink-0">
                    {/* Placeholder do avatar do usuário logado */}
                </div>
                <div className="flex-1">
          <textarea
              value={conteudo}
              onChange={(e) => setConteudo(e.target.value)}
              placeholder="O que está acontecendo?"
              className="w-full bg-transparent text-white text-xl placeholder-gray-500 focus:outline-none resize-none"
              rows={2}
              disabled={isLoading}
          />
                    {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
                    <div className="flex justify-end mt-2">
                        <Button type="submit" isLoading={isLoading} disabled={!conteudo.trim() || isLoading} className="w-auto px-6">
                            Postar
                        </Button>
                    </div>
                </div>
            </form>
        </div>
    );
}