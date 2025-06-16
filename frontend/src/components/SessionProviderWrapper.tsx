// src/components/SessionProviderWrapper.tsx
'use client'; // Marca este componente como um Client Component

import { SessionProvider } from 'next-auth/react';
import React from 'react';

// Props para aceitar componentes filhos
interface Props {
    children: React.ReactNode;
}

// Componente que renderiza o SessionProvider
export default function SessionProviderWrapper({ children }: Props) {
    return (
        <SessionProvider>
            {children}
        </SessionProvider>
    );
}