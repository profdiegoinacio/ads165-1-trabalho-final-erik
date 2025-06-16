// src/auth.config.ts

// PASSO 1: Importamos os tipos que precisamos do next-auth
import type { User, Session } from 'next-auth';
import type { JWT } from 'next-auth/jwt';

export const authConfig = {
    providers: [],
    pages: {
        signIn: '/login',
    },
    callbacks: {
        // PASSO 2: Adicionamos os tipos explícitos para os parâmetros
        async jwt({ token, user }: { token: JWT; user?: User }) { // 'user' é opcional
            if (user) {
                // Graças ao nosso next-auth.d.ts, TypeScript sabe que 'user' pode ter accessToken, etc.
                token.accessToken = user.accessToken;
                token.roles = user.roles;
                token.username = user.username;
            }
            return token;
        },
        // PASSO 3: Adicionamos os tipos explícitos aqui também
        async session({ session, token }: { session: Session; token: JWT }) {
            // Graças ao next-auth.d.ts, TypeScript sabe que 'session.user' pode ter nossas propriedades
            if (session.user) {
                if (token.accessToken) {
                    session.accessToken = token.accessToken as string;
                }
                if (token.roles) {
                    session.user.roles = token.roles as string[];
                }
                if (token.username) {
                    session.user.username = token.username as string;
                    session.user.name = token.username as string;
                }
            }
            return session;
        },
    },
};