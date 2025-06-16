// frontend/src/app/layout.tsx

import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
// --- PASSO 1: Importe o Wrapper ---
import SessionProviderWrapper from "@/components/SessionProviderWrapper";

const geistSans = Geist({
    variable: "--font-geist-sans",
    subsets: ["latin"],
});

const geistMono = Geist_Mono({
    variable: "--font-geist-mono",
    subsets: ["latin"],
});

// (Sugestão: Atualize os metadados para o seu projeto)
export const metadata: Metadata = {
    title: "Conecta Pro",
    description: "Conectando profissionais e clientes",
};

export default function RootLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        // (Sugestão: Mude para "pt-BR" se o idioma principal for português)
        <html lang="pt-BR">
        <body
            className={`${geistSans.variable} ${geistMono.variable} antialiased`}
        >
        {/* --- PASSO 2: Envolva o {children} com o Wrapper --- */}
        <SessionProviderWrapper>
            {children}
        </SessionProviderWrapper>
        {/* ---------------------------------------------------- */}
        </body>
        </html>
    );
}