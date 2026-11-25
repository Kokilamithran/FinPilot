import { create } from 'zustand';
import { transactionApi } from '../api/transactionApi';

export const useTransactionStore = create((set) => ({
  transactions: [],
  isLoading: false,
  error: null,

  fetchTransactions: async () => {
    set({ isLoading: true, error: null });
    try {
      const data = await transactionApi.getAll();
      set({ transactions: data, isLoading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message || 'Failed to load transactions', isLoading: false });
    }
  },

  createTransaction: async (transactionData) => {
    set({ isLoading: true, error: null });
    try {
      const newTx = await transactionApi.create(transactionData);
      set((state) => ({ transactions: [newTx, ...state.transactions], isLoading: false }));
      return newTx;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to create transaction';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  updateTransaction: async (id, transactionData) => {
    set({ isLoading: true, error: null });
    try {
      const updated = await transactionApi.update(id, transactionData);
      set((state) => ({
        transactions: state.transactions.map((tx) => (tx.transactionId === id ? updated : tx)),
        isLoading: false,
      }));
      return updated;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to update transaction';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  deleteTransaction: async (id) => {
    set({ isLoading: true, error: null });
    try {
      await transactionApi.delete(id);
      set((state) => ({
        transactions: state.transactions.filter((tx) => tx.transactionId !== id),
        isLoading: false,
      }));
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to delete transaction';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },
}));
