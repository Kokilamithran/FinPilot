import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Mail, Lock, Compass, ArrowRight } from 'lucide-react';
import { useAuthStore } from '../../store/useAuthStore';
import { Button } from '../../components/common/Button';
import { Input } from '../../components/common/Input';
import { ErrorAlert } from '../../components/common/ErrorAlert';

export const LoginPage = () => {
  const navigate = useNavigate();
  const { login, isLoading, error, clearError } = useAuthStore();

  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const handleChange = (e) => {
    if (error) clearError();
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const success = await login(formData);
    if (success) {
      navigate('/');
    }
  };

  return (
    <div className="min-h-screen bg-[#f8fafc] flex items-center justify-center p-4 relative overflow-hidden">
      {/* Background Glow Elements */}
      <div className="absolute top-1/4 left-1/2 -translate-x-1/2 w-[500px] h-[500px] bg-emerald-500/5 rounded-full blur-[120px] pointer-events-none" />
      <div className="absolute bottom-10 right-10 w-[300px] h-[300px] bg-teal-500/5 rounded-full blur-[100px] pointer-events-none" />

      <div className="w-full max-w-md bg-white rounded-3xl p-8 shadow-xl border border-slate-200 relative z-10">
        {/* Header */}
        <div className="text-center mb-8">
          <div className="inline-flex p-3 rounded-2xl bg-emerald-500 text-white shadow-md shadow-emerald-500/20 border border-emerald-400/20 mb-4">
            <Compass className="w-8 h-8" />
          </div>
          <h1 className="text-2xl font-extrabold text-slate-900">Welcome to FinPilot</h1>
          <p className="text-xs text-slate-500 mt-1">Sign in to access your financial cockpit</p>
        </div>

        <ErrorAlert message={error} onClose={clearError} />

        <form onSubmit={handleSubmit} className="space-y-5">
          <Input
            label="Email Address"
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="alex.morgan@example.com"
            icon={Mail}
            required
          />

          <Input
            label="Password"
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="••••••••"
            icon={Lock}
            required
          />

          <Button
            type="submit"
            variant="primary"
            size="lg"
            isLoading={isLoading}
            className="w-full"
            icon={ArrowRight}
          >
            Sign In
          </Button>
        </form>

        <div className="mt-8 text-center pt-6 border-t border-slate-100">
          <p className="text-xs text-slate-500">
            Don't have an account?{' '}
            <Link to="/register" className="font-semibold text-emerald-600 hover:text-emerald-700 hover:underline">
              Create an account
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};
