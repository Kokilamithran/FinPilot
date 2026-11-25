import apiClient from './axios';

export const recurringApi = {
  getAll: async () => {
    const response = await apiClient.get('/api/recurring-transactions');
    return response.data;
  },

  create: async (data) => {
    const response = await apiClient.post('/api/recurring-transactions', data);
    return response.data;
  },

  update: async (id, data) => {
    const response = await apiClient.put(`/api/recurring-transactions/${id}`, data);
    return response.data;
  },

  delete: async (id) => {
    const response = await apiClient.delete(`/api/recurring-transactions/${id}`);
    return response.data;
  },
};
