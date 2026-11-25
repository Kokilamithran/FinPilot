import React from 'react';

export const Badge = ({
  children,
  variant = 'neutral',
  size = 'md',
  className = '',
}) => {
  const variants = {
    emerald: 'bg-emerald-50 text-emerald-700 border-emerald-200/80 font-semibold',
    rose: 'bg-rose-50 text-rose-700 border-rose-200/80 font-semibold',
    indigo: 'bg-indigo-50 text-indigo-700 border-indigo-200/80 font-semibold',
    amber: 'bg-amber-50 text-amber-700 border-amber-200/80 font-semibold',
    sky: 'bg-sky-50 text-sky-700 border-sky-200/80 font-semibold',
    neutral: 'bg-slate-100 text-slate-700 border-slate-200 font-medium',
  };

  const sizes = {
    sm: 'px-2 py-0.5 text-[10px]',
    md: 'px-2.5 py-1 text-xs',
    lg: 'px-3 py-1.5 text-sm',
  };

  return (
    <span
      className={`inline-flex items-center font-semibold rounded-full border ${variants[variant]} ${sizes[size]} ${className}`}
    >
      {children}
    </span>
  );
};
