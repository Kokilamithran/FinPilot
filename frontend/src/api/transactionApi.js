import apiClient from './axios';

export const transactionApi = {
  getAll: async () => {
    const response = await apiClient.get('/api/transactions');
    return response.data;
  },

  getById: async (id) => {
    const response = await apiClient.get(`/api/transactions/${id}`);
    return response.data;
  },

  create: async (transactionData) => {
    const response = await apiClient.post('/api/transactions', transactionData);
    return response.data;
  },

  update: async (id, transactionData) => {
    const response = await apiClient.put(`/api/transactions/${id}`, transactionData);
    return response.data;
  },

  delete: async (id) => {
    const response = await apiClient.delete(`/api/transactions/${id}`);
    return response.data;
  },
};
