import React, { forwardRef } from 'react';

export const Select = forwardRef(({
  label,
  options = [],
  error,
  icon: Icon,
  className = '',
  id,
  required = false,
  placeholder = 'Select an option',
  ...props
}, ref) => {
  const selectId = id || (label ? label.toLowerCase().replace(/\s+/g, '-') : undefined);

  return (
    <div className="w-full">
      {label && (
        <label htmlFor={selectId} className="block text-xs font-semibold text-slate-700 mb-1.5">
          {label} {required && <span className="text-rose-500">*</span>}
        </label>
      )}
      <div className="relative">
        {Icon && (
          <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
            <Icon className="w-4 h-4" />
          </div>
        )}
        <select
          ref={ref}
          id={selectId}
          className={`w-full bg-white border border-slate-200 rounded-xl px-3.5 py-2.5 text-sm text-slate-900 shadow-xs transition-all appearance-none cursor-pointer focus:outline-none ${
            Icon ? 'pl-10' : ''
          } ${
            error
              ? 'border-rose-300 focus:border-rose-500 focus:ring-2 focus:ring-rose-500/20'
              : 'focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20'
          } ${className}`}
          {...props}
        >
          {placeholder && <option value="" className="bg-white text-slate-400">{placeholder}</option>}
          {options.map((opt) => (
            <option
              key={opt.value}
              value={opt.value}
              className="bg-white text-slate-900"
            >
              {opt.label}
            </option>
          ))}
        </select>
        <div className="absolute inset-y-0 right-0 pr-3.5 flex items-center pointer-events-none text-slate-400">
          <svg className="w-4 h-4 fill-current" viewBox="0 0 20 20">
            <path d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" />
          </svg>
        </div>
      </div>
      {error && <p className="mt-1 text-xs text-rose-600 font-medium">{error}</p>}
    </div>
  );
});

Select.displayName = 'Select';
