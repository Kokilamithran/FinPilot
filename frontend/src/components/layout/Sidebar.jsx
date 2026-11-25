import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  LayoutDashboard,
  Wallet,
  ArrowLeftRight,
  Tags,
  PieChart,
  Target,
  Repeat,
  Users,
  Bell,
  FileSpreadsheet,
  LogOut,
  X,
  Compass,
} from 'lucide-react';
import { useAuthStore } from '../../store/useAuthStore';

const navigationItems = [
  { name: 'Dashboard', path: '/', icon: LayoutDashboard },
  { name: 'Accounts', path: '/accounts', icon: Wallet },
  { name: 'Transactions', path: '/transactions', icon: ArrowLeftRight },
  { name: 'Categories', path: '/categories', icon: Tags },
  { name: 'Budgets', path: '/budgets', icon: PieChart },
  { name: 'Financial Goals', path: '/goals', icon: Target },
  { name: 'Recurring', path: '/recurring', icon: Repeat },
  { name: 'Expense Groups', path: '/groups', icon: Users },
  { name: 'Notifications', path: '/notifications', icon: Bell },
  { name: 'Import History', path: '/imports', icon: FileSpreadsheet },
];

export const Sidebar = ({ isOpen, onClose }) => {
  const logout = useAuthStore((state) => state.logout);

  return (
    <>
      {/* Mobile Backdrop */}
      {isOpen && (
        <div
          className="fixed inset-0 z-40 bg-slate-950/80 backdrop-blur-sm lg:hidden"
          onClick={onClose}
        />
      )}

      {/* Sidebar Container */}
      <aside
        className={`fixed top-0 bottom-0 left-0 z-50 w-64 bg-[#0b132b] border-r border-slate-800/80 flex flex-col transition-transform duration-300 ease-in-out lg:translate-x-0 ${
          isOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        {/* Header / Logo */}
        <div className="flex items-center justify-between h-16 px-6 border-b border-slate-800/80">
          <div className="flex items-center gap-3">
            <div className="p-2 rounded-xl bg-emerald-500 text-white shadow-md shadow-emerald-500/20 border border-emerald-400/20">
              <Compass className="w-5 h-5" />
            </div>
            <div>
              <h1 className="text-lg font-extrabold text-white">
                Fin<span className="text-emerald-400">Pilot</span>
              </h1>
              <span className="text-[10px] text-slate-400 uppercase tracking-widest font-semibold block">Finance OS</span>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1 rounded-lg text-slate-400 hover:text-slate-100 lg:hidden"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Navigation Links */}
        <div className="flex-1 overflow-y-auto px-4 py-4 space-y-1.5">
          <div className="text-[11px] font-bold text-slate-400 uppercase tracking-wider px-3 mb-2 opacity-90">
            Overview & Core
          </div>
          {navigationItems.slice(0, 4).map((item) => (
            <SidebarLink key={item.path} item={item} onClick={onClose} />
          ))}

          <div className="text-[11px] font-bold text-slate-400 uppercase tracking-wider px-3 mt-6 mb-2 opacity-90">
            Planning & Split
          </div>
          {navigationItems.slice(4, 8).map((item) => (
            <SidebarLink key={item.path} item={item} onClick={onClose} />
          ))}

          <div className="text-[11px] font-bold text-slate-400 uppercase tracking-wider px-3 mt-6 mb-2 opacity-90">
            System & Logs
          </div>
          {navigationItems.slice(8).map((item) => (
            <SidebarLink key={item.path} item={item} onClick={onClose} />
          ))}
        </div>

        {/* Footer / Logout */}
        <div className="p-4 border-t border-slate-800/80">
          <button
            onClick={logout}
            className="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium text-slate-400 hover:text-rose-400 hover:bg-rose-500/10 transition-colors"
          >
            <LogOut className="w-4 h-4" />
            <span>Sign Out</span>
          </button>
        </div>
      </aside>
    </>
  );
};

const SidebarLink = ({ item, onClick }) => {
  const Icon = item.icon;
  return (
    <NavLink
      to={item.path}
      onClick={onClick}
      className={({ isActive }) =>
        `flex items-center gap-3 px-3.5 py-2.5 rounded-xl text-sm font-semibold transition-all ${
          isActive
            ? 'bg-[#10b981] text-white shadow-sm shadow-emerald-500/20'
            : 'text-slate-400 hover:text-white hover:bg-slate-800/60'
        }`
      }
    >
      <Icon className="w-4 h-4 shrink-0" />
      <span>{item.name}</span>
    </NavLink>
  );
};
