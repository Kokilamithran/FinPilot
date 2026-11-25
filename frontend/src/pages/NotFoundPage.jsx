import React from 'react';
import { Link } from 'react-router-dom';
import { AlertTriangle, Home } from 'lucide-react';
import { Button } from '../components/common/Button';

export const NotFoundPage = () => {
  return (
    <div className="min-h-[70vh] flex flex-col items-center justify-center text-center p-6">
      <div className="p-4 rounded-full bg-indigo-500/10 text-indigo-400 mb-4 border border-indigo-500/20">
        <AlertTriangle className="w-10 h-10" />
      </div>
      <h1 className="text-3xl font-extrabold text-slate-100 mb-2">Page Not Found</h1>
      <p className="text-sm text-slate-400 max-w-md mb-6">
        The page you are looking for does not exist or has been moved.
      </p>
      <Link to="/">
        <Button icon={Home}>Return to Dashboard</Button>
      </Link>
    </div>
  );
};

