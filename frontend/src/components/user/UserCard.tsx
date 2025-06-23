// src/components/user/UserCard.tsx
import Link from 'next/link';
import Image from 'next/image';
import { SearchedUser } from '@/types/user';

export default function UserCard({ user }: { user: SearchedUser }) {
    return (
        <Link href={`/dashboard/perfil/${user.nomeUsuario}`} className="block hover:bg-gray-800/50 p-4 rounded-lg border border-gray-800 transition-colors">
            <div className="flex items-center space-x-4">
                <div className="w-12 h-12 bg-gray-600 rounded-full relative overflow-hidden">
                    {user.fotoPerfilUrl && (
                        <Image src={user.fotoPerfilUrl} alt={`Foto de ${user.nome}`} layout="fill" objectFit="cover" />
                    )}
                </div>
                <div className="flex-1">
                    <p className="font-bold text-white">{user.nome}</p>
                    <p className="text-sm text-gray-400">@{user.nomeUsuario}</p>
                </div>
            </div>
        </Link>
    );
}