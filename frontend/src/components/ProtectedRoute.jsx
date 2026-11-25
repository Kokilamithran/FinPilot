import React, { useEffect } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';
import { LoadingSpinner } from './common/LoadingSpinner';

export const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, isLoading, fetchMe } = useAuthStore();
  const location = useLocation();

  useEffect(() => {
    fetchMe();
  }, []);

  if (isLoading) {
    return <LoadingSpinner fullScreen label="Authenticating session..." />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return children;
};
