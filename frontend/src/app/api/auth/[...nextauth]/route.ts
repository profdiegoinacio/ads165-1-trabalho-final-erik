// O caminho completo é: src/app/api/auth/[...nextauth]/route.ts

// Importa os handlers do seu arquivo de configuração principal do NextAuth
import { handlers } from '@/auth'; // Verifique se o path para seu arquivo auth.ts está correto

// Exporta os métodos GET e POST. O NextAuth cuidará do resto.
export const { GET, POST } = handlers;