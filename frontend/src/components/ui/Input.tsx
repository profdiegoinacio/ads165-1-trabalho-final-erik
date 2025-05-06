import React, { InputHTMLAttributes } from 'react';


interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
    label?: string; // Label opcional
    id: string; // ID é obrigatório para o htmlFor do label
}

const Input: React.FC<InputProps> = ({ label, id, className, ...props }) => {
    return (
        <div className="w-full">
            {label && (
                <label htmlFor={id} className="block text-sm font-medium text-gray-400 mb-1">
                    {label}
                </label>
            )}
            <input
                id={id}
                className={`w-full p-3 bg-gray-800 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-white placeholder-gray-500 ${className}`} // Combina classes padrão com as passadas via props
                {...props} // Passa todas as outras props (type, name, value, onChange, placeholder, required, etc.)
            />
        </div>
    );
};

export default Input;