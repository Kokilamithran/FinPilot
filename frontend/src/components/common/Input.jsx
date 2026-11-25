import React, { forwardRef } from 'react';

export const Input = forwardRef(({
  label,
  error,
  icon: Icon,
  type = 'text',
  className = '',
  id,
  required = false,
  ...props
}, ref) => {
  const inputId = id || (label ? label.toLowerCase().replace(/\s+/g, '-') : undefined);

  return (
    <div className="w-full">
      {label && (
        <label htmlFor={inputId} className="block text-xs font-semibold text-slate-700 mb-1.5">
          {label} {required && <span className="text-rose-500">*</span>}
        </label>
      )}
      <div className="relative">
        {Icon && (
          <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
            <Icon className="w-4 h-4" />
          </div>
        )}
        <input
          ref={ref}
          id={inputId}
          type={type}
          className={`w-full bg-white border border-slate-200 rounded-xl px-3.5 py-2.5 text-sm text-slate-900 placeholder:text-slate-400 shadow-xs transition-all ${
            Icon ? 'pl-10' : ''
          } ${
            error
              ? 'border-rose-300 focus:border-rose-500 focus:ring-2 focus:ring-rose-500/20'
              : 'focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20'
          } ${className}`}
          {...props}
        />
      </div>
      {error && <p className="mt-1 text-xs text-rose-600 font-medium">{error}</p>}
    </div>
  );
});

Input.displayName = 'Input';
