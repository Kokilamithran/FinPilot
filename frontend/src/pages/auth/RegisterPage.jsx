import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { User, Mail, Lock, Phone, Compass, UserPlus } from 'lucide-react';
import { useAuthStore } from '../../store/useAuthStore';
import { Button } from '../../components/common/Button';
import { Input } from '../../components/common/Input';
import { ErrorAlert } from '../../components/common/ErrorAlert';

export const RegisterPage = () => {
  const navigate = useNavigate();
  const { register, isLoading, error, clearError } = useAuthStore();

  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: '',
    mobile: '',
  });

  const [successMsg, setSuccessMsg] = useState('');

  const handleChange = (e) => {
    if (error) clearError();
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.password || formData.password.length < 6) {
      useAuthStore.setState({ error: 'Password must be at least 6 characters long' });
      return;
    }
    try {
      await register(formData);
      setSuccessMsg('Account created successfully! Redirecting to login...');
      setTimeout(() => {
        navigate('/login');
      }, 1500);
    } catch (err) {
      // Error is set in store
    }
  };

  return (
    <div className="min-h-screen bg-[#f8fafc] flex items-center justify-center p-4 relative overflow-hidden">
      <div className="absolute top-1/4 left-1/2 -translate-x-1/2 w-[500px] h-[500px] bg-emerald-500/5 rounded-full blur-[120px] pointer-events-none" />

      <div className="w-full max-w-md bg-white rounded-3xl p-8 shadow-xl border border-slate-200 relative z-10">
        <div className="text-center mb-8">
          <div className="inline-flex p-3 rounded-2xl bg-emerald-500 text-white shadow-md shadow-emerald-500/20 border border-emerald-400/20 mb-4">
            <Compass className="w-8 h-8" />
          </div>
          <h1 className="text-2xl font-extrabold text-slate-900">Create your Account</h1>
          <p className="text-xs text-slate-500 mt-1">Start tracking and managing your finances</p>
        </div>

        {successMsg && (
          <div className="p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm mb-4">
            {successMsg}
          </div>
        )}

        <ErrorAlert message={error} onClose={clearError} />

        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Full Name"
            name="fullName"
            value={formData.fullName}
            onChange={handleChange}
            placeholder="Alex Morgan"
            icon={User}
            required
          />

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
            label="Mobile Number"
            type="tel"
            name="mobile"
            value={formData.mobile}
            onChange={handleChange}
            placeholder="+91 98765 43210"
            icon={Phone}
          />

          <Input
            label="Password"
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="Minimum 6 characters"
            icon={Lock}
            minLength={6}
            required
          />

          <Button
            type="submit"
            variant="primary"
            size="lg"
            isLoading={isLoading}
            className="w-full mt-2"
            icon={UserPlus}
          >
            Create Account
          </Button>
        </form>

        <div className="mt-8 text-center pt-6 border-t border-slate-100">
          <p className="text-xs text-slate-500">
            Already have an account?{' '}
            <Link to="/login" className="font-semibold text-emerald-600 hover:text-emerald-700 hover:underline">
              Sign In
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};
