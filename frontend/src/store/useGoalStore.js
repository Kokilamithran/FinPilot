import { create } from 'zustand';
import { goalApi } from '../api/goalApi';

export const useGoalStore = create((set) => ({
  goals: [],
  contributions: [],
  isLoading: false,
  error: null,

  fetchGoals: async () => {
    set({ isLoading: true, error: null });
    try {
      const data = await goalApi.getAllGoals();
      set({ goals: data, isLoading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message || 'Failed to load goals', isLoading: false });
    }
  },

  fetchContributions: async () => {
    set({ isLoading: true, error: null });
    try {
      const data = await goalApi.getAllContributions();
      set({ contributions: data, isLoading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message || 'Failed to load contributions', isLoading: false });
    }
  },

  createGoal: async (goalData) => {
    set({ isLoading: true, error: null });
    try {
      const newGoal = await goalApi.createGoal(goalData);
      set((state) => ({ goals: [...state.goals, newGoal], isLoading: false }));
      return newGoal;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to create goal';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  createContribution: async (contribData) => {
    set({ isLoading: true, error: null });
    try {
      const newContrib = await goalApi.createContribution(contribData);
      set((state) => ({ contributions: [...state.contributions, newContrib], isLoading: false }));
      return newContrib;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to create contribution';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  deleteGoal: async (id) => {
    set({ isLoading: true, error: null });
    try {
      await goalApi.deleteGoal(id);
      set((state) => ({
        goals: state.goals.filter((g) => g.goalId !== id),
        isLoading: false,
      }));
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to delete goal';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },
}));
