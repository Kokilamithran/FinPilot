import React from 'react';
import { AlertCircle } from 'lucide-react';

export const ErrorAlert = ({ message, onClose }) => {
  if (!message) return null;

  return (
    <div className="flex items-center justify-between p-4 rounded-xl bg-rose-50 border border-rose-200 text-rose-700 text-sm mb-4">
      <div className="flex items-center gap-3">
        <AlertCircle className="w-5 h-5 shrink-0" />
        <span className="font-medium">{message}</span>
      </div>
      {onClose && (
        <button
          onClick={onClose}
          className="text-xs font-semibold hover:underline focus:outline-none"
        >
          Dismiss
        </button>
      )}
    </div>
  );
};
