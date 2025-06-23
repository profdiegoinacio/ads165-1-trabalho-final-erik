"use client";

import { useState, FormEvent, useEffect } from 'react';
import api from '@/lib/api';
import { PerfilUsuario, AreaDeAtuacao } from '@/types';

interface ModalProps {
    isOpen: boolean;
    onClose: () => void;
    profileData: PerfilUsuario;
    onProfileUpdate: () => void;
    todasAsAreas: AreaDeAtuacao[];
}

export default function ModalEditarPerfil({ isOpen, onClose, profileData, onProfileUpdate, todasAsAreas }: ModalProps) {

    const [formData, setFormData] = useState({
        nome: profileData.nome,
        nomeUsuario: profileData.nomeUsuario,
        bio: profileData.bio || '',
        formacao: profileData.formacao || '',
        fotoPerfilUrl: profileData.fotoPerfilUrl || '',
        fotoCapaUrl: profileData.fotoCapaUrl || '',
        isProfissional: profileData.isProfissional || false,
    });

    const [selectedAreaIds, setSelectedAreaIds] = useState<Set<number>>(
        new Set(profileData.areasDeAtuacao?.map(area => area.id) || [])
    );

    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        if (isOpen) {
            setFormData({
                nome: profileData.nome,
                nomeUsuario: profileData.nomeUsuario,
                bio: profileData.bio || '',
                formacao: profileData.formacao || '',
                fotoPerfilUrl: profileData.fotoPerfilUrl || '',
                fotoCapaUrl: profileData.fotoCapaUrl || '',
                isProfissional: profileData.isProfissional || false,
            });
            setSelectedAreaIds(new Set(profileData.areasDeAtuacao?.map(area => area.id) || []));
        }
    }, [isOpen, profileData]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value, type } = e.target;
        const isCheckbox = type === 'checkbox';
        setFormData(prev => ({
            ...prev,
            [name]: isCheckbox ? (e.target as HTMLInputElement).checked : value
        }));
    };

    const handleCheckboxChange = (areaId: number) => {
        const newSelectedAreaIds = new Set(selectedAreaIds);
        if (newSelectedAreaIds.has(areaId)) {
            newSelectedAreaIds.delete(areaId);
        } else {
            newSelectedAreaIds.add(areaId);
        }
        setSelectedAreaIds(newSelectedAreaIds);
    };

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        setIsLoading(true);

        const dadosParaAtualizar = {
            ...formData,
            areasDeAtuacaoIds: Array.from(selectedAreaIds)
        };

        try {
            await api.put('/usuarios/perfil', dadosParaAtualizar);
            alert('Perfil atualizado com sucesso!');
            onProfileUpdate();
            onClose();
        } catch (error) {
            console.error("Erro ao atualizar perfil:", error);
            alert('Falha ao atualizar o perfil.');
        } finally {
            setIsLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-75 flex justify-center items-center z-50 overflow-y-auto p-4">
            <div className="bg-gray-900 text-white p-6 rounded-lg w-full max-w-lg">
                <h2 className="text-2xl font-bold mb-4">Editar Perfil</h2>
                <form onSubmit={handleSubmit}>

                    <div className="my-4">
                        <label htmlFor="nome">Nome Completo</label>
                        <input name="nome" id="nome" value={formData.nome} onChange={handleInputChange} className="w-full p-2 bg-gray-800 rounded mt-1"/>
                    </div>
                    <div className="my-4">
                        <label htmlFor="bio">Bio</label>
                        <textarea name="bio" id="bio" value={formData.bio} onChange={handleInputChange} className="w-full p-2 bg-gray-800 rounded mt-1"/>
                    </div>
                    <div className="my-4">
                        <label htmlFor="formacao">Formação</label>
                        <input name="formacao" id="formacao" value={formData.formacao} onChange={handleInputChange} className="w-full p-2 bg-gray-800 rounded mt-1"/>
                    </div>
                    <div className="my-4">
                        <label htmlFor="fotoPerfilUrl">URL da Foto de Perfil</label>
                        <input name="fotoPerfilUrl" id="fotoPerfilUrl" value={formData.fotoPerfilUrl} onChange={handleInputChange} className="w-full p-2 bg-gray-800 rounded mt-1"/>
                    </div>
                    <div className="my-4">
                        <label htmlFor="fotoCapaUrl">URL da Foto de Capa</label>
                        <input name="fotoCapaUrl" id="fotoCapaUrl" value={formData.fotoCapaUrl} onChange={handleInputChange} className="w-full p-2 bg-gray-800 rounded mt-1"/>
                    </div>
                    <div className="my-4">
                        <label className="flex items-center space-x-2 cursor-pointer">
                            <input type="checkbox" name="isProfissional" checked={formData.isProfissional} onChange={handleInputChange} />
                            <span>Sou um profissional</span>
                        </label>
                    </div>

                    <h3 className="text-lg font-semibold mt-6 mb-2">Suas Áreas de Atuação</h3>
                    <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
                        {todasAsAreas.map((area) => (
                            <div key={area.id}>
                                <label className="flex items-center space-x-2 cursor-pointer">
                                    <input type="checkbox" checked={selectedAreaIds.has(area.id)} onChange={() => handleCheckboxChange(area.id)} />
                                    <span>{area.nome}</span>
                                </label>
                            </div>
                        ))}
                    </div>

                    <div className="flex justify-end space-x-4 mt-6">
                        <button type="button" onClick={onClose} className="px-4 py-2 bg-gray-700 rounded hover:bg-gray-600">Cancelar</button>
                        <button type="submit" disabled={isLoading} className="px-4 py-2 bg-blue-600 rounded hover:bg-blue-500 disabled:bg-gray-500">
                            {isLoading ? 'Salvando...' : 'Salvar'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}