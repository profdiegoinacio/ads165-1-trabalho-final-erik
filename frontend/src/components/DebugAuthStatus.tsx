// src/components/DebugAuthStatus.tsx
'use client'; // Este componente precisa ser de cliente para usar o hook
import { useSession } from 'next-auth/react';

export default function DebugAuthStatus() {
    // Pega os dados da sessão e o status do carregamento
    const { data: session, status } = useSession();

    return (
        <div style={{
            backgroundColor: '#111827', // bg-gray-900
            color: '#d1d5db',       // text-gray-300
            padding: '1rem',
            margin: '1rem',
            borderRadius: '8px',
            border: '1px solid #374151', // border-gray-700
            fontFamily: 'monospace',
            whiteSpace: 'pre-wrap',      // Mantém a formatação do JSON
            wordBreak: 'break-all'
        }}>
            <h3 style={{ color: 'white', fontWeight: 'bold', borderBottom: '1px solid #4b5563', paddingBottom: '0.5rem', marginBottom: '0.5rem' }}>
                Auth Debug Status
            </h3>
            <p><span style={{ color: 'white' }}>Status:</span> {status}</p>
            <p style={{ color: 'white', marginTop: '1rem' }}>Session Object:</p>
            {/* Imprime o objeto da sessão inteiro como um JSON formatado */}
            <pre>{JSON.stringify(session, null, 2)}</pre>
        </div>
    );
}