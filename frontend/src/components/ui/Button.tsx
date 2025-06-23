import React, { ButtonHTMLAttributes } from 'react';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
    isLoading?: boolean;
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
            disabled={isLoading || disabled}
            {...props}
        >
            {isLoading ? 'Processando...' : children}
        </button>
    );
};

export default Button;