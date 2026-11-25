import { create } from 'zustand';
import { categoryApi } from '../api/categoryApi';

export const useCategoryStore = create((set) => ({
  categories: [],
  isLoading: false,
  error: null,

  fetchCategories: async () => {
    set({ isLoading: true, error: null });
    try {
      const data = await categoryApi.getAll();
      set({ categories: data, isLoading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message || 'Failed to load categories', isLoading: false });
    }
  },

  createCategory: async (categoryData) => {
    set({ isLoading: true, error: null });
    try {
      const newCategory = await categoryApi.create(categoryData);
      set((state) => ({ categories: [...state.categories, newCategory], isLoading: false }));
      return newCategory;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to create category';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  updateCategory: async (id, categoryData) => {
    set({ isLoading: true, error: null });
    try {
      const updated = await categoryApi.update(id, categoryData);
      set((state) => ({
        categories: state.categories.map((cat) => (cat.categoryId === id ? updated : cat)),
        isLoading: false,
      }));
      return updated;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to update category';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },

  deleteCategory: async (id) => {
    set({ isLoading: true, error: null });
    try {
      await categoryApi.delete(id);
      set((state) => ({
        categories: state.categories.filter((cat) => cat.categoryId !== id),
        isLoading: false,
      }));
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to delete category';
      set({ error: msg, isLoading: false });
      throw new Error(msg);
    }
  },
}));
