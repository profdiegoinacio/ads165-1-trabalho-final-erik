import type { User, Session } from 'next-auth';
import type { JWT } from 'next-auth/jwt';

export const authConfig = {
    providers: [],
    pages: {
        signIn: '/login',
    },
    callbacks: {
        async jwt({ token, user }: { token: JWT; user?: User }) {
            if (user) {
                token.accessToken = user.accessToken;
                token.roles = user.roles;
                token.username = user.username;
            }
            return token;
        },
        async session({ session, token }: { session: Session; token: JWT }) {
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