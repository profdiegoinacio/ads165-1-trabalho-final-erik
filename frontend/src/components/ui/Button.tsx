import React, { ButtonHTMLAttributes } from 'react';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
    // Adicione variantes se necessário (ex: primary, secondary)
    isLoading?: boolean; // Para mostrar estado de carregamento
}

const Button: React.FC<ButtonProps> = ({
                                           children,
                                           className,
                                           isLoading = false,
                                           disabled,
                                           ...props
                                       }) => {
    return (
        <button
            className={`w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-md transition duration-300 ease-in-out disabled:opacity-50 disabled:cursor-not-allowed ${className}`}
            disabled={isLoading || disabled} // Desabilita se isLoading ou se já estava disabled
            {...props} // Passa outras props (type, onClick, etc.)
        >
            {isLoading ? 'Processando...' : children}
        </button>
    );
};

export default Button;