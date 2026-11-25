import { create } from 'zustand';
import { budgetApi } from '../api/budgetApi';

export const useBudgetStore = create((set) => ({
  budgets: [],
  budgetItems: [],
  isLoading: false,
  error: null,

  fetchBudgets: async () => {
    set({ isLoading: true, error: null });
    try {
      const data = await budgetApi.getAllBudgets();
      set({ budgets: data, isLoading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message || 'Failed to load budgets', isLoading: false });
    }
  },

  fetchBudgetItems: async () => {
    set({ isLoading: true, error: null });
    try {
      const data = await budgetApi.getAllBudgetItems();
      set({ budgetItems: data, isLoading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message || 'Failed to load budget items', isLoading: false });
    }
  },

  createBudget: async (budgetData) => {
    set({ isLoading: true, error: null });
    try {
      const newBudget = await budgetApi.createBudget(budgetData);
      set((state) => ({ budgets: [...state.budgets, newBudget], isLoading: false }));
      return newBudget;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to create budget';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  createBudgetItem: async (itemData) => {
    set({ isLoading: true, error: null });
    try {
      const newItem = await budgetApi.createBudgetItem(itemData);
      set((state) => ({ budgetItems: [...state.budgetItems, newItem], isLoading: false }));
      return newItem;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to create budget item';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  updateBudgetItem: async (id, itemData) => {
    set({ isLoading: true, error: null });
    try {
      const updated = await budgetApi.updateBudgetItem(id, itemData);
      set((state) => ({
        budgetItems: state.budgetItems.map((item) => ((item.itemId || item.budgetItemId) === id ? updated : item)),
        isLoading: false,
      }));
      return updated;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to update budget item';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  deleteBudgetItem: async (id) => {
    set({ isLoading: true, error: null });
    try {
      await budgetApi.deleteBudgetItem(id);
      set((state) => ({
        budgetItems: state.budgetItems.filter((item) => (item.itemId || item.budgetItemId) !== id),
        isLoading: false,
      }));
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to delete budget item';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  deleteBudget: async (id) => {
    set({ isLoading: true, error: null });
    try {
      await budgetApi.deleteBudget(id);
      set((state) => ({
        budgets: state.budgets.filter((b) => b.budgetId !== id),
        isLoading: false,
      }));
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to delete budget';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },
}));
