import React from 'react';

export const LoadingSpinner = ({ label = 'Loading...', fullScreen = false }) => {
  const content = (
    <div className="flex flex-col items-center justify-center p-8 gap-3">
      <div className="relative w-10 h-10">
        <div className="absolute inset-0 rounded-full border-2 border-emerald-200"></div>
        <div className="absolute inset-0 rounded-full border-2 border-transparent border-t-emerald-600 animate-spin"></div>
      </div>
      {label && <p className="text-xs font-medium text-slate-500">{label}</p>}
    </div>
  );

  if (fullScreen) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-50">
        {content}
      </div>
    );
  }

  return content;
};
