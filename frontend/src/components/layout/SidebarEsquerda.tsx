'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useSession, signOut } from 'next-auth/react';
import { Home, User, Users, Briefcase, History, Shapes, LogOut, MessageSquare } from 'lucide-react';

const NavLink = ({ href, icon, children }: { href: string; icon: React.ReactNode; children: React.ReactNode }) => {
    const pathname = usePathname();
    const isActive = pathname === href;

    return (
        <Link href={href}>
            <span className={`flex items-center space-x-4 px-3 py-2 rounded-full transition-colors duration-200 hover:bg-gray-800 ${isActive ? 'font-bold text-white' : 'text-gray-300'}`}>
                {icon}
                <span className="text-lg">{children}</span>
            </span>
        </Link>
    );
};


export default function SidebarEsquerda() {
    const { data: session } = useSession();

    return (
        <div className="h-full flex flex-col justify-between p-2">
            <div>
                <div className="p-4">
                    <h1 className="text-3xl font-bold text-blue-400 p-3 tracking-wider">
                        Conecta
                    </h1>
                </div>

                <nav className="space-y-2">
                    <NavLink href="/dashboard" icon={<Home size={28} />}>Página Inicial</NavLink>
                    <NavLink href="/dashboard/profissionais" icon={<Briefcase size={28} />}>Profissionais</NavLink>
                    <NavLink href="/dashboard/comunidade" icon={<Users size={28} />}>Comunidade</NavLink>
                    <NavLink href="/dashboard/mensagens" icon={<MessageSquare size={28} />}>Mensagens</NavLink>
                    <NavLink href="/dashboard/historico" icon={<History size={28} />}>Histórico</NavLink>
                    <NavLink href="/dashboard/categorias" icon={<Shapes size={28} />}>Categorias</NavLink>
                    {session?.user?.username && (
                        <NavLink href={`/dashboard/perfil/${session.user.username}`} icon={<User size={28} />}>
                            Perfil
                        </NavLink>
                    )}
                </nav>
            </div>


            {session && (
                <div className="p-4">
                    <button
                        onClick={() => signOut({ callbackUrl: '/login' })}
                        className="w-full flex items-center justify-center space-x-3 px-3 py-3 rounded-full font-bold text-lg bg-red-600 text-white hover:bg-red-700 transition-colors"
                    >
                        <LogOut size={24} />
                        <span>Sair</span>
                    </button>
                </div>
            )}
        </div>
    );
}
