import React from 'react';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
}

const Input = ({ label, className = '', ...props }: InputProps) => {
  return (
    <div className="flex flex-col space-y-1">
      {label && <label className="text-sm font-semibold text-gray-700">{label}</label>}
      <input 
        className={`border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 ${className}`}
        {...props}
      />
    </div>
  );
};

export default Input;