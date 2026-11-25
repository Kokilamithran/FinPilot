import { create } from 'zustand';
import { authApi } from '../api/authApi';

export const useAuthStore = create((set, get) => ({
  user: null,
  token: localStorage.getItem('finpilot_token') || null,
  isAuthenticated: false,
  isLoading: true,
  error: null,

  login: async (credentials) => {
    set({ isLoading: true, error: null });
    try {
      const data = await authApi.login(credentials);
      if (data.token) {
        localStorage.setItem('finpilot_token', data.token);
        set({ token: data.token });
        await get().fetchMe();
        return true;
      }
      throw new Error(data.message || 'Login failed');
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message || 'Invalid email or password';
      set({ error: errorMessage, isLoading: false, isAuthenticated: false });
      return false;
    }
  },

  register: async (userData) => {
    set({ isLoading: true, error: null });
    try {
      const data = await authApi.register(userData);
      set({ isLoading: false });
      return data;
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message || 'Registration failed';
      set({ error: errorMessage, isLoading: false });
      throw new Error(errorMessage);
    }
  },

  fetchMe: async () => {
    const token = localStorage.getItem('finpilot_token');
    if (!token) {
      set({ user: null, isAuthenticated: false, isLoading: false });
      return;
    }

    set({ isLoading: true, error: null });
    try {
      const userData = await authApi.getCurrentUser();
      set({
        user: userData,
        isAuthenticated: true,
        isLoading: false,
      });
    } catch (err) {
      localStorage.removeItem('finpilot_token');
      set({
        user: null,
        token: null,
        isAuthenticated: false,
        isLoading: false,
        error: null,
      });
    }
  },

  logout: () => {
    localStorage.removeItem('finpilot_token');
    set({
      user: null,
      token: null,
      isAuthenticated: false,
      isLoading: false,
      error: null,
    });
  },

  clearError: () => set({ error: null }),
}));
