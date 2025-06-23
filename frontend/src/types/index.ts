
export interface AreaDeAtuacao {
    id: number;
    nome: string;
}

export interface PostAutor {
    nome: string;
    nomeUsuario: string;
    fotoPerfilUrl: string;
}

export interface Post {
    id: number;
    conteudo: string;
    urlMidia?: string;
    dataCriacao: string;
    autor: PostAutor;
}

export interface PerfilUsuario {
    id: number;
    nome: string;
    nomeUsuario: string;
    email: string;
    roles: string[];
    notaMedia: number | null;
    totalAvaliacoes: number;
    totalSeguidores: number;
    totalSeguindo: number;
    seguindoPeloUsuarioLogado: boolean;
    bio: string;
    formacao: string;
    fotoPerfilUrl: string;
    fotoCapaUrl: string;
    isProfissional: boolean;
    areasDeAtuacao: AreaDeAtuacao[];
}