// src/components/dashboard/Postagem.tsx
import { Post } from '@/types/post'; // Importa nossa tipagem
import React from 'react';
import Image from 'next/image';

interface PostagemProps {
    post: Post;
}

export default function Postagem({ post }: PostagemProps) {
    return (
        <article className="p-4 border-b border-gray-700 hover:bg-gray-900/50 transition-colors duration-200 flex space-x-4">
            {/* Avatar */}
            <div className="flex-shrink-0">
                <div className="w-12 h-12 bg-gray-600 rounded-full">
                    {/* Placeholder para a foto do autor */}
                </div>
            </div>

            {/* Conteúdo do Post */}
            <div className="flex-1">
                <div className="flex items-center space-x-2">
                    <span className="font-bold text-white">{post.autor.nome}</span>
                    <span className="text-gray-500">@{post.autor.nomeUsuario}</span>
                    <span className="text-gray-500">·</span>
                    <span className="text-gray-500 text-sm">
            {/* Formatação básica de data */}
                        {new Date(post.dataCriacao).toLocaleDateString('pt-BR', {
                            hour: '2-digit',
                            minute: '2-digit',
                        })}
          </span>
                </div>

                <p className="mt-2 text-white whitespace-pre-wrap">{post.conteudo}</p>

                {/* Placeholder para mídia (imagem/vídeo) */}
                {post.urlMidia && (
                    <div className="mt-3 rounded-xl border border-gray-700 overflow-hidden">
                        <Image src={post.urlMidia} alt="Mídia da postagem" width={500} height={300} className="object-cover w-full" />
                    </div>
                )}

                {/* Ícones de Ação */}
                <div className="flex justify-between mt-4 text-gray-500 max-w-xs">
                    <button className="flex items-center space-x-2 hover:text-blue-500">
                        {/* Ícone de Comentário */}
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path></svg>
                        <span>0</span>
                    </button>
                    <button className="flex items-center space-x-2 hover:text-green-500">
                        {/* Ícone de Compartilhar/Retweet */}
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 4v5h5M20 20v-5h-5M4 20h5v-5M20 4h-5v5"></path></svg>
                        <span>0</span>
                    </button>
                    <button className="flex items-center space-x-2 hover:text-pink-500">
                        {/* Ícone de Curtir */}
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4.318 6.318a4.5 4.5 0 010 6.364L12 20.364l7.682-7.682a4.5 4.5 0 01-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 010-6.364z"></path></svg>
                        <span>0</span>
                    </button>
                    <button className="hover:text-blue-500">
                        {/* Ícone de Compartilhar */}
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"></path></svg>
                    </button>
                </div>
            </div>
        </article>
    );
}