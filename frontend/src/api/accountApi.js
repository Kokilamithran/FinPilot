import apiClient from './axios';

export const accountApi = {
  getAll: async () => {
    const response = await apiClient.get('/api/accounts');
    return response.data;
  },

  getById: async (id) => {
    const response = await apiClient.get(`/api/accounts/${id}`);
    return response.data;
  },

  create: async (accountData) => {
    const response = await apiClient.post('/api/accounts', accountData);
    return response.data;
  },

  update: async (id, accountData) => {
    const response = await apiClient.put(`/api/accounts/${id}`, accountData);
    return response.data;
  },

  delete: async (id) => {
    const response = await apiClient.delete(`/api/accounts/${id}`);
    return response.data;
  },
};
