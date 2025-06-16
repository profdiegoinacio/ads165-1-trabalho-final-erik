// src/auth.ts
import NextAuth from "next-auth";
import Credentials from "next-auth/providers/credentials";
import { authConfig } from "./auth.config"; // Importa a configuração base
import type { User } from "next-auth";

interface BackendLoginResponse {
    token: string;
    username: string;
    roles: string[]; // Esperamos um array de strings
}

export const {
    handlers,
    auth,
    signIn,
    signOut,
} = NextAuth({
    ...authConfig,
    providers: [
        Credentials({
            async authorize(credentials) {
                if (!credentials?.login || !credentials?.password) return null;

                try {
                    const res = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/auth/login`, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            login: credentials.login,
                            senha: credentials.password,
                        }),
                    });

                    if (!res.ok) return null;

                    const backendResponse = await res.json() as BackendLoginResponse;

                    if (backendResponse?.token) {
                        // --- LÓGICA DEFENSIVA APLICADA AQUI ---
                        const user: User = {
                            id: backendResponse.username,
                            name: backendResponse.username,
                            // Nossas propriedades customizadas
                            username: backendResponse.username,
                            // Garante que 'roles' seja sempre um array, mesmo que o backend retorne null
                            roles: backendResponse.roles || [],
                            accessToken: backendResponse.token,
                        };
                        return user;
                    }
                    return null;
                } catch (error) {
                    console.error("Erro na chamada ao backend:", error);
                    return null;
                }
            },
        }),
    ],
});