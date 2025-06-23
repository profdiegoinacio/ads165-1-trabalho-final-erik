"use client";

import React, { useState, FormEvent } from 'react';
import { useSession } from 'next-auth/react';
import api from '@/lib/api';
import Button from '../ui/Button';
import StarRatingInput from '../ui/StarRatingInput';

interface ModalProps {
    isOpen: boolean;
    onClose: () => void;
    profileOwnerUsername: string;
    onReviewSubmitted: () => void;
}

export default function ModalDeixarAvaliacao({ isOpen, onClose, profileOwnerUsername, onReviewSubmitted }: ModalProps) {
    const [nota, setNota] = useState(0);
    const [comentario, setComentario] = useState('');

    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const { data: session } = useSession();

    if (!isOpen) return null;

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault();
        if (nota === 0) {
            setError("Por favor, selecione uma nota (de 1 a 5 estrelas).");
            return;
        }
        setIsLoading(true);
        setError(null);

        const token = session?.accessToken;
        if (!token) {
            setError("Sessão inválida. Por favor, faça login novamente.");
            setIsLoading(false);
            return;
        }

        try {
            const reviewData = { nota, comentario };

            await api.post(`/usuarios/${profileOwnerUsername}/avaliacoes`, reviewData, {
                headers: { Authorization: `Bearer ${token}` },
            });

            onReviewSubmitted();
            onClose();

        } catch (err: any) {
            console.error("Erro ao enviar avaliação:", err);
            const errorMessage = err.response?.data?.message || err.response?.data || "Não foi possível enviar sua avaliação.";
            setError(errorMessage);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 bg-black bg-opacity-70 flex justify-center items-center z-50 p-4">
            <div className="bg-gray-900 p-8 rounded-lg shadow-xl w-full max-w-lg border border-gray-700">
                <h2 className="text-2xl font-bold mb-6 text-white">Deixar uma Avaliação</h2>
                <form onSubmit={handleSubmit} className="space-y-4">
                    {error && <p className="text-red-500 text-center text-sm">{error}</p>}

                    <div>
                        <label className="block text-sm font-medium text-gray-400 mb-2">Sua nota</label>
                        <StarRatingInput rating={nota} setRating={setNota} />
                    </div>

                    <div>
                        <label htmlFor="comentario" className="block text-sm font-medium text-gray-400 mb-1">Seu comentário (opcional)</label>
                        <textarea
                            id="comentario"
                            value={comentario}
                            onChange={(e) => setComentario(e.target.value)}
                            placeholder="Descreva sua experiência com este profissional..."
                            rows={5}
                            className="w-full p-3 bg-gray-800 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-white"
                        />
                    </div>

                    <div className="flex justify-end space-x-4 pt-4">
                        <Button type="button" onClick={onClose} disabled={isLoading} className="bg-gray-600 hover:bg-gray-700 w-auto px-6">
                            Cancelar
                        </Button>
                        <Button type="submit" isLoading={isLoading} disabled={isLoading || nota === 0} className="w-auto px-6">
                            Enviar Avaliação
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    );
}