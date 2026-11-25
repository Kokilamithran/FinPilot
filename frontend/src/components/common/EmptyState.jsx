import React from 'react';
import { PackageOpen } from 'lucide-react';
import { Button } from './Button';

export const EmptyState = ({
  icon: Icon = PackageOpen,
  title = 'No records found',
  description = 'Get started by creating your first entry.',
  actionLabel,
  onAction,
}) => {
  return (
    <div className="flex flex-col items-center justify-center p-12 text-center border border-dashed border-slate-200 rounded-2xl bg-white shadow-xs">
      <div className="p-4 rounded-2xl bg-emerald-50 text-emerald-600 border border-emerald-100 mb-4 shadow-xs">
        <Icon className="w-8 h-8" />
      </div>
      <h4 className="text-base font-bold text-slate-900">{title}</h4>
      <p className="text-xs text-slate-500 max-w-sm mt-1 mb-6">{description}</p>
      {actionLabel && onAction && (
        <Button onClick={onAction} size="sm">
          {actionLabel}
        </Button>
      )}
    </div>
  );
};
