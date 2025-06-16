// src/types/post.ts

export interface Autor {
    id: number;
    nome: string;
    nomeUsuario: string;
}

export interface Post {
    id: number;
    conteudo: string;
    urlMidia?: string | null;
    dataCriacao: string; // Vem como string no JSON, podemos formatar depois
    autor: Autor;
}