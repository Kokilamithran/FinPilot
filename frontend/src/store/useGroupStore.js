import { create } from 'zustand';
import { groupApi } from '../api/groupApi';

export const useGroupStore = create((set) => ({
  groups: [],
  members: [],
  groupExpenses: [],
  expenseShares: [],
  isLoading: false,
  error: null,

  fetchGroups: async () => {
    set({ isLoading: true, error: null });
    try {
      const data = await groupApi.getAllGroups();
      set({ groups: data, isLoading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message || 'Failed to load groups', isLoading: false });
    }
  },

  fetchGroupExpenses: async () => {
    set({ isLoading: true, error: null });
    try {
      const data = await groupApi.getAllGroupExpenses();
      set({ groupExpenses: data, isLoading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message || 'Failed to load group expenses', isLoading: false });
    }
  },

  createGroup: async (groupData) => {
    set({ isLoading: true, error: null });
    try {
      const newGroup = await groupApi.createGroup(groupData);
      set((state) => ({ groups: [...state.groups, newGroup], isLoading: false }));
      return newGroup;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to create group';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  createGroupExpense: async (expenseData) => {
    set({ isLoading: true, error: null });
    try {
      const newExp = await groupApi.createGroupExpense(expenseData);
      set((state) => ({ groupExpenses: [...state.groupExpenses, newExp], isLoading: false }));
      return newExp;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to create group expense';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },
}));
