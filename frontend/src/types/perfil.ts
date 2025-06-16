// src/types/perfil.ts
export interface PerfilUsuario {
    id: number;
    nome: string;
    nomeUsuario: string;
    email: string;
    roles: string[];
    bio?: string;
    formacao?: string;
    fotoPerfilUrl?: string;
    fotoCapaUrl?: string;
    notaMedia?: number;
    totalAvaliacoes?: number;
}