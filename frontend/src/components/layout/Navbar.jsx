import React from 'react';
import { Menu, User as UserIcon, Shield, Currency } from 'lucide-react';
import { useAuthStore } from '../../store/useAuthStore';

export const Navbar = ({ onMenuClick }) => {
  const user = useAuthStore((state) => state.user);

  return (
    <header className="h-16 bg-white border-b border-slate-200/80 sticky top-0 z-30 px-4 sm:px-6 flex items-center justify-between shadow-xs">
      <div className="flex items-center gap-3">
        <button
          onClick={onMenuClick}
          className="p-2 rounded-xl text-slate-500 hover:text-slate-900 hover:bg-slate-100 lg:hidden"
        >
          <Menu className="w-5 h-5" />
        </button>
        <div>
          <h2 className="text-sm font-bold text-slate-900">
            Welcome back, {user?.fullName || 'User'}
          </h2>
          <p className="text-xs text-slate-500">
            {user?.email || ''}
          </p>
        </div>
      </div>

      <div className="flex items-center gap-3">
        {/* Currency Preference Badge */}
        <div className="hidden sm:flex items-center gap-1.5 px-3 py-1 rounded-full bg-slate-100 border border-slate-200 text-xs text-slate-700 font-medium">
          <Currency className="w-3.5 h-3.5 text-emerald-600" />
          <span>{user?.currencyPreference || 'INR'}</span>
        </div>

        {/* User Role Badge */}
        <div className="flex items-center gap-1.5 px-3 py-1 rounded-full bg-emerald-50 border border-emerald-200/80 text-xs text-emerald-700 font-semibold">
          <Shield className="w-3.5 h-3.5" />
          <span>{user?.role || 'USER'}</span>
        </div>

        {/* User Avatar */}
        <div className="w-9 h-9 rounded-full bg-emerald-600 flex items-center justify-center text-white font-bold text-sm shadow-xs border border-emerald-400/30">
          {user?.fullName ? user.fullName.charAt(0).toUpperCase() : <UserIcon className="w-4 h-4" />}
        </div>
      </div>
    </header>
  );
};
