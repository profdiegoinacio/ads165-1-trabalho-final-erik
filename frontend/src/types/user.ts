interface PerfilResumido {
    bio?: string;
    formacao?: string;
    fotoPerfilUrl?: string;
    fotoCapaUrl?: string;
    isProfissional: boolean;
}

export interface UsuarioResponseDTO {
    id: number;
    nome: string;
    nomeUsuario: string;
    email: string;
    perfil?: PerfilResumido;
}

export interface SearchedUser {
    id: number;
    nome: string;
    nomeUsuario: string;
    fotoPerfilUrl?: string | null;
}