// O caminho completo é: src/next-auth.d.ts

import 'next-auth';
import { DefaultSession, DefaultUser } from 'next-auth';
import { JWT as DefaultJWT } from 'next-auth/jwt';

// Usamos 'declare module' para "aumentar" a definição de tipos de um módulo existente.
declare module 'next-auth' {
    /**
     * O objeto 'user' da sessão, que é exposto para o cliente via useSession().
     * Aqui definimos quais dados do usuário estarão disponíveis no frontend.
     */
    interface Session {
        accessToken?: string; // Nosso token JWT do backend
        user: {
            roles?: string[];
            username?: string;
        } & DefaultSession['user']; // "&" faz um merge com os tipos padrão (name, email, image)
    }

    /**
     * O objeto 'User' é o que retornamos da função 'authorize' do nosso CredentialsProvider.
     * É o primeiro objeto que criamos após o login bem-sucedido no backend.
     */
    interface User extends DefaultUser {
        accessToken?: string;
        roles?: string[];
        username?: string;
    }
}

// Também precisamos estender o tipo do token JWT interno do NextAuth
declare module 'next-auth/jwt' {
    /**
     * O token JWT interno do NextAuth.js. O callback 'jwt' trabalha com este tipo.
     * Nós passamos os dados do objeto User para este token.
     */
    interface JWT extends DefaultJWT {
        accessToken?: string;
        roles?: string[];
        username?: string;
    }
}