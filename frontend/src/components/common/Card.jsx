import React from 'react';

export const Card = ({
  children,
  className = '',
  title,
  subtitle,
  action,
  icon: Icon,
  hoverable = false,
}) => {
  return (
    <div
      className={`bg-white border border-slate-200/80 shadow-xs rounded-2xl p-5 sm:p-6 ${
        hoverable ? 'glass-card-hover' : ''
      } ${className}`}
    >
      {(title || action || Icon) && (
        <div className="flex items-center justify-between mb-4 pb-3.5 border-b border-slate-100">
          <div className="flex items-center gap-3">
            {Icon && (
              <div className="p-2.5 rounded-xl bg-emerald-50 text-emerald-600 border border-emerald-100 shadow-xs">
                <Icon className="w-5 h-5" />
              </div>
            )}
            <div>
              {title && <h3 className="text-base font-bold text-slate-900">{title}</h3>}
              {subtitle && <p className="text-xs text-slate-500 mt-0.5">{subtitle}</p>}
            </div>
          </div>
          {action && <div>{action}</div>}
        </div>
      )}
      {children}
    </div>
  );
};
