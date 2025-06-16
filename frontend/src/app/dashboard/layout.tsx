// src/app/dashboard/layout.tsx
import React from 'react';
import SidebarEsquerda from '@/components/layout/SidebarEsquerda';
import SidebarDireita from '@/components/layout/SidebarDireita';

export default function DashboardLayout({
                                            children,
                                        }: {
    children: React.ReactNode;
}) {
    return (
        // Container principal que ocupa a tela toda
        <div className="min-h-screen bg-black text-white">
            {/* Container que centraliza o conteúdo e define o layout flex */}
            <div className="container mx-auto flex justify-center max-w-screen-lg">
                {/* Coluna da Esquerda (Menu) */}
                <SidebarEsquerda />

                {/* Coluna Central (Conteúdo Principal / Feed com scroll) */}
                <main className="w-full max-w-[600px] border-x border-gray-800">
                    {/* O conteúdo da page.tsx do dashboard será renderizado aqui */}
                    {children}
                </main>

                {/* Coluna da Direita (Busca e Destaques) */}
                <SidebarDireita />
            </div>
        </div>
    );
}