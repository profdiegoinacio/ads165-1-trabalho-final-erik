import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    images: {
        remotePatterns: [
            {
                protocol: 'https',
                hostname: '**', // O wildcard duplo permite QUALQUER hostname
            },
            {
                protocol: 'http',
                hostname: '**', // Também para http, se necessário
            },
        ],
    },
};

export default nextConfig;
