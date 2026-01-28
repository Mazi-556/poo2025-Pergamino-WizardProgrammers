import React from 'react';

interface AlertProps {
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  onClose?: () => void;
  className?: string;
}

export const Alert: React.FC<AlertProps> = ({ type, message, onClose, className = '' }) => {
  const typeStyles = {
    success: 'bg-green-50 text-green-800 border-green-200',
    error: 'bg-red-50 text-red-800 border-red-200',
    warning: 'bg-yellow-50 text-yellow-800 border-yellow-200',
    info: 'bg-blue-50 text-blue-800 border-blue-200',
  };

  const iconStyles = {
    success: '✓',
    error: '✕',
    warning: '⚠',
    info: 'ℹ',
  };

  return (
    <div className={`p-4 rounded-lg border ${typeStyles[type]} flex items-start gap-3 ${className}`}>
      <span className="text-lg font-bold flex-shrink-0">{iconStyles[type]}</span>
      <span className="flex-grow">{message}</span>
      {onClose && (
        <button
          onClick={onClose}
          className="flex-shrink-0 text-xl hover:opacity-70 transition"
        >
          ×
        </button>
      )}
    </div>
  );
};
