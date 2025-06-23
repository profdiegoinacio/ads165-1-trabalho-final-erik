import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios';
import { getSession, signOut } from 'next-auth/react';


interface CustomAxiosRequestConfig extends InternalAxiosRequestConfig {
    _retry?: boolean;
}

const api = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_BASE_URL,
});

api.interceptors.request.use(
    async (config) => {
        const session = await getSession();

        if (session?.accessToken) {
            config.headers.Authorization = `Bearer ${session.accessToken}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

api.interceptors.response.use(
    (response) => response,
    async (error: AxiosError) => {

        const originalRequest = error.config as CustomAxiosRequestConfig;

        if (error.response?.status === 401 && originalRequest && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                const session = await getSession();
                if (!session?.refreshToken) {
                    await signOut();
                    return Promise.reject(error);
                }

                const { data } = await axios.post('/api/auth/refresh-token', {
                    refreshToken: session.refreshToken,
                });

                const newAccessToken = data.accessToken;

                if (originalRequest.headers) {
                    originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                }

                return api(originalRequest);
            } catch (refreshError) {
                await signOut({ redirect: true, callbackUrl: '/login' });
                return Promise.reject(refreshError);
            }
        }

        return Promise.reject(error);
    }
);

export default api;