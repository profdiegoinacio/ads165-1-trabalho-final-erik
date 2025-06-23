export interface Autor {
    nome: string;
    nomeUsuario: string;
    fotoPerfilUrl?: string;
}

export interface Post {
    id: number;
    conteudo: string;
    dataCriacao: string;
    urlMidia?: string;
    autor: Autor;
}
