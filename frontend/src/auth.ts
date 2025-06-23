import NextAuth from "next-auth";
import Credentials from "next-auth/providers/credentials";
import type { NextAuthConfig } from "next-auth";
import type { User } from "next-auth";

interface BackendLoginResponse {
    token: string;
    username: string;
    roles: string[];
}

export const authConfig: NextAuthConfig = {
    providers: [
        Credentials({
            name: "credentials",
            credentials: {
                login: { label: "Login", type: "text" },
                password: { label: "Senha", type: "password" },
            },
            async authorize(credentials) {
                if (!credentials?.login || !credentials?.password) {
                    console.error("Tentativa de login sem credenciais completas.");
                    return null;
                }

                try {
                    const res = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/auth/login`, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            login: credentials.login,
                            senha: credentials.password,
                        }),
                    });

                    if (!res.ok) {
                        const errorBody = await res.text();
                        console.error("Falha na autenticação com o backend:", res.status, errorBody);
                        return null;
                    }

                    const backendResponse: BackendLoginResponse = await res.json();

                    if (backendResponse && backendResponse.token) {
                        const user: User = {
                            id: backendResponse.username,
                            name: backendResponse.username,
                            username: backendResponse.username,
                            roles: backendResponse.roles,
                            accessToken: backendResponse.token,
                        };
                        return user;
                    }

                    return null;
                } catch (error) {
                    console.error("Erro na chamada de autorização ao backend:", error);
                    return null;
                }
            },
        }),
    ],
    callbacks: {

        async jwt({ token, user }) {
            if (user) {
                token.accessToken = user.accessToken;
                token.roles = user.roles;
                token.username = user.username;
            }
            return token;
        },

        async session({ session, token }) {
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
            if (token.sub) {
                session.user.id = token.sub;
            }
            return session;
        },
    },
    session: { strategy: "jwt" },
    pages: { signIn: '/login' },
    secret: process.env.AUTH_SECRET,
    debug: process.env.NODE_ENV === 'development',
};

export const { handlers, auth, signIn, signOut } = NextAuth(authConfig);
