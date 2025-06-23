import 'next-auth';
import { DefaultSession, DefaultUser } from 'next-auth';
import { JWT as DefaultJWT } from 'next-auth/jwt';

declare module 'next-auth' {

    interface Session {
        accessToken?: string;
        refreshToken?: string; // <-- ADICIONAR AQUI

        user: {
            id: string;
            username?: string;
            roles?: string[];
        } & DefaultSession['user'];
    }

    interface User extends DefaultUser {
        accessToken?: string;
        refreshToken?: string; // <-- ADICIONAR AQUI
        username?: string;
        roles?: string[];
    }
}

declare module 'next-auth/jwt' {

    interface JWT extends DefaultJWT {
        accessToken?: string;
        refreshToken?: string; // <-- ADICIONAR AQUI
        username?: string;
        roles?: string[];
    }
}