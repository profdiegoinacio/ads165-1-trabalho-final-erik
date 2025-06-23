'use client';

import { Post } from '@/types/post';
import Image from 'next/image';
import { MessageCircle, Heart, Repeat, Share, User as UserIcon } from 'lucide-react'; // Adicionado UserIcon
import { formatDistanceToNow, parseISO } from 'date-fns';
import { ptBR } from 'date-fns/locale';

interface PostagemProps {
    post: Post;
}

export default function Postagem({ post }: PostagemProps) {

    const timeAgo = formatDistanceToNow(parseISO(post.dataCriacao), {
        addSuffix: true,
        locale: ptBR,
    });

    return (
        <div className="flex space-x-4 p-4 border-b border-gray-800">
            <div className="flex-shrink-0">
                <div className="w-12 h-12 bg-gray-700 rounded-full relative overflow-hidden">
                    {post.autor?.fotoPerfilUrl ? (
                        <Image
                            src={post.autor.fotoPerfilUrl || '/default-avatar.png'}
                            alt={`Foto de ${post.autor.nome}`}
                            width={40}
                            height={40}
                            className="rounded-full"
                        />
                    ) : (
                        <div className="w-full h-full bg-gray-800 flex items-center justify-center">
                            <UserIcon size={24} className="text-gray-500" />
                        </div>
                    )}
                </div>
            </div>
            <div className="flex-1">
                <div className="flex items-center space-x-2 text-sm">
                    <p className="font-bold text-white">{post.autor.nome}</p>
                    <p className="text-gray-500">@{post.autor.nomeUsuario}</p>
                    <span className="text-gray-500">·</span>
                    <p className="text-gray-500">{timeAgo}</p>
                </div>

                <p className="mt-1 text-white whitespace-pre-wrap">{post.conteudo}</p>

                {post.urlMidia && (
                    <div className="mt-3 rounded-2xl border border-gray-700 overflow-hidden">
                        <Image src={post.urlMidia} alt="Mídia da postagem" width={500} height={300} objectFit="cover" />
                    </div>
                )}

                <div className="flex justify-between items-center mt-4 text-gray-500 max-w-sm">
                    <button className="flex items-center space-x-2 hover:text-blue-500 transition-colors">
                        <MessageCircle size={18} />
                        <span className="text-xs">0</span>
                    </button>
                    <button className="flex items-center space-x-2 hover:text-green-500 transition-colors">
                        <Repeat size={18} />
                        <span className="text-xs">0</span>
                    </button>
                    <button className="flex items-center space-x-2 hover:text-red-500 transition-colors">
                        <Heart size={18} />
                        <span className="text-xs">0</span>
                    </button>
                    <button className="flex items-center space-x-2 hover:text-blue-500 transition-colors">
                        <Share size={18} />
                    </button>
                </div>
            </div>
        </div>
    );
}
