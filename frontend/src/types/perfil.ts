export interface AreaDeAtuacao {
    id: number;
    nome: string;
}

export interface PerfilUsuario {
    areasDeAtuacao: any;
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
    seguindoPeloUsuarioLogado: boolean;
    totalSeguidores: number;
    totalSeguindo: number;
    isProfissional: boolean;
}