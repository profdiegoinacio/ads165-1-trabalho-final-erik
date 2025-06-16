// src/components/layout/SidebarEsquerda.tsx
import Link from 'next/link';
import React from 'react';

export default function SidebarEsquerda() {
    // Itens do menu (podem vir de um array de configuração no futuro)
    const menuItems = [
        { href: '/dashboard', label: 'Home' },
        { href: '/profissionais', label: 'Profissionais' },
        { href: '/comunidade', label: 'Comunidade' },
        { href: '/historico', label: 'Histórico' },
        { href: '/categorias', label: 'Categorias' },
    ];

    return (
        <aside className="w-64 p-4 sticky top-0 h-screen hidden lg:flex flex-col">
            <div className="mb-8">
                {/* Logo */}
                <Link href="/dashboard" className="text-2xl font-bold text-white">ONECTA</Link>
            </div>

            <nav className="flex flex-col space-y-2">
                {menuItems.map((item) => (
                    <Link
                        key={item.href}
                        href={item.href}
                        className="text-lg text-gray-300 hover:bg-gray-800 hover:text-white rounded-full px-4 py-2 transition-colors duration-200"
                    >
                        {item.label}
                    </Link>
                ))}
            </nav>

            {/* Botão de Perfil na parte inferior */}
            <div className="mt-auto">
                <Link href="/dashboard/perfil/meu-perfil" // Link para o perfil do usuário logado (exemplo)
                      className="flex items-center space-x-3 p-2 hover:bg-gray-800 rounded-full"
                >
                    <div className="w-10 h-10 bg-gray-600 rounded-full"></div> {/* Placeholder do Avatar */}
                    <span className="font-semibold text-white">Perfil</span>
                </Link>
            </div>
        </aside>
    );
}