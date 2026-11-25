import { create } from 'zustand';
import { accountApi } from '../api/accountApi';

export const useAccountStore = create((set, get) => ({
  accounts: [],
  isLoading: false,
  error: null,

  fetchAccounts: async () => {
    set({ isLoading: true, error: null });
    try {
      const data = await accountApi.getAll();
      set({ accounts: data, isLoading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message || 'Failed to load accounts', isLoading: false });
    }
  },

  createAccount: async (accountData) => {
    set({ isLoading: true, error: null });
    try {
      const newAccount = await accountApi.create(accountData);
      set((state) => ({ accounts: [...state.accounts, newAccount], isLoading: false }));
      return newAccount;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to create account';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  updateAccount: async (id, accountData) => {
    set({ isLoading: true, error: null });
    try {
      const updated = await accountApi.update(id, accountData);
      set((state) => ({
        accounts: state.accounts.map((acc) => (acc.accountId === id ? updated : acc)),
        isLoading: false,
      }));
      return updated;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to update account';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  deleteAccount: async (id) => {
    set({ isLoading: true, error: null });
    try {
      await accountApi.delete(id);
      set((state) => ({
        accounts: state.accounts.filter((acc) => acc.accountId !== id),
        isLoading: false,
      }));
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to delete account';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },
}));
