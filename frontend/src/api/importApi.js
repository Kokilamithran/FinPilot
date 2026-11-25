import apiClient from './axios';

export const importApi = {
  getAll: async () => {
    const response = await apiClient.get('/api/import-history');
    return response.data;
  },

  create: async (data) => {
    const response = await apiClient.post('/api/import-history', data);
    return response.data;
  },

  delete: async (id) => {
    const response = await apiClient.delete(`/api/import-history/${id}`);
    return response.data;
  },
};
