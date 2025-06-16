// src/components/perfil/ModalEditarPerfil.tsx
"use client";

import React, { useState, useEffect, FormEvent } from 'react';
import { useSession } from 'next-auth/react';
import { PerfilUsuario } from '@/types/perfil';
import api from '@/lib/api';
import Input from '../ui/Input';
import Button from '../ui/Button';

interface ModalProps {
    isOpen: boolean;
    onClose: () => void;
    profileData: PerfilUsuario;
    onProfileUpdate: (updatedProfile: PerfilUsuario) => void;
}

export default function ModalEditarPerfil({ isOpen, onClose, profileData, onProfileUpdate }: ModalProps) {
    // Estados para cada campo do formulário
    const [nome, setNome] = useState('');
    const [nomeUsuario, setNomeUsuario] = useState('');
    const [bio, setBio] = useState('');
    const [formacao, setFormacao] = useState('');
    const [fotoPerfilUrl, setFotoPerfilUrl] = useState('');
    const [fotoCapaUrl, setFotoCapaUrl] = useState('');

    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const { data: session } = useSession();

    // Preenche o formulário com os dados atuais quando o modal abre ou os dados mudam
    useEffect(() => {
        if (profileData) {
            setNome(profileData.nome || '');
            setNomeUsuario(profileData.nomeUsuario || '');
            setBio(profileData.bio || '');
            setFormacao(profileData.formacao || '');
            setFotoPerfilUrl(profileData.fotoPerfilUrl || '');
            setFotoCapaUrl(profileData.fotoCapaUrl || '');
        }
    }, [profileData, isOpen]); // Roda quando o modal abre

    if (!isOpen) return null;

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault();
        setIsLoading(true);
        setError(null);

        const token = session?.accessToken;
        if (!token) {
            setError("Sessão inválida.");
            setIsLoading(false);
            return;
        }

        try {
            // Vamos fazer as duas chamadas de atualização
            const perfilUpdateData = { bio, formacao, fotoPerfilUrl, fotoCapaUrl };
            const usuarioUpdateData = { nome, nomeUsuario };

            // Promise.all executa as duas requisições em paralelo
            const [perfilResponse, usuarioResponse] = await Promise.all([
                api.put('/perfis/me', perfilUpdateData, { headers: { Authorization: `Bearer ${token}` } }),
                api.patch('/usuarios/me/dados-principais', usuarioUpdateData, { headers: { Authorization: `Bearer ${token}` } })
            ]);

            // Usamos a resposta do PATCH de usuário, que contém todos os dados atualizados
            onProfileUpdate(usuarioResponse.data);
            onClose();

        } catch (err: any) {
            console.error("Erro ao atualizar perfil:", err);
            const errorMessage = err.response?.data?.message || err.response?.data || "Não foi possível atualizar o perfil.";
            setError(errorMessage);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 bg-black bg-opacity-70 flex justify-center items-center z-50 p-4">
            <div className="bg-gray-900 p-6 rounded-lg shadow-xl w-full max-w-lg border border-gray-700 max-h-[90vh] overflow-y-auto">
                <h2 className="text-2xl font-bold mb-6 text-white">Editar Perfil</h2>
                <form onSubmit={handleSubmit} className="space-y-4">
                    {error && <p className="text-red-500 text-center text-sm">{error}</p>}

                    <Input id="nome" label="Nome" type="text" value={nome} onChange={(e) => setNome(e.target.value)} />
                    <Input id="nomeUsuario" label="Nome de Usuário (@)" type="text" value={nomeUsuario} onChange={(e) => setNomeUsuario(e.target.value)} />

                    <div>
                        <label htmlFor="bio" className="block text-sm font-medium text-gray-400 mb-1">Biografia</label>
                        <textarea id="bio" value={bio} onChange={(e) => setBio(e.target.value)} rows={4} className="w-full p-3 bg-gray-800 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-white" />
                    </div>

                    <Input id="formacao" label="Formação / Especialidade" type="text" value={formacao} onChange={(e) => setFormacao(e.target.value)} />
                    <Input id="fotoPerfilUrl" label="URL da Foto de Perfil" type="text" value={fotoPerfilUrl} onChange={(e) => setFotoPerfilUrl(e.target.value)} />
                    <Input id="fotoCapaUrl" label="URL da Foto de Capa" type="text" value={fotoCapaUrl} onChange={(e) => setFotoCapaUrl(e.target.value)} />

                    <div className="flex justify-end space-x-4 pt-4">
                        <Button type="button" onClick={onClose} disabled={isLoading} className="bg-gray-600 hover:bg-gray-700 w-auto px-6">Cancelar</Button>
                        <Button type="submit" isLoading={isLoading} disabled={isLoading} className="w-auto px-6">Salvar Alterações</Button>
                    </div>
                </form>
            </div>
        </div>
    );
}