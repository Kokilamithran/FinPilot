import apiClient from './axios';

export const budgetApi = {
  // Budgets
  getAllBudgets: async () => {
    const response = await apiClient.get('/api/budgets');
    return response.data;
  },

  createBudget: async (budgetData) => {
    const response = await apiClient.post('/api/budgets', budgetData);
    return response.data;
  },

  updateBudget: async (id, budgetData) => {
    const response = await apiClient.put(`/api/budgets/${id}`, budgetData);
    return response.data;
  },

  deleteBudget: async (id) => {
    const response = await apiClient.delete(`/api/budgets/${id}`);
    return response.data;
  },

  // Budget Items
  getAllBudgetItems: async () => {
    const response = await apiClient.get('/api/budget-items');
    return response.data;
  },

  createBudgetItem: async (itemData) => {
    const response = await apiClient.post('/api/budget-items', itemData);
    return response.data;
  },

  updateBudgetItem: async (id, itemData) => {
    const response = await apiClient.put(`/api/budget-items/${id}`, itemData);
    return response.data;
  },

  deleteBudgetItem: async (id) => {
    const response = await apiClient.delete(`/api/budget-items/${id}`);
    return response.data;
  },
};
