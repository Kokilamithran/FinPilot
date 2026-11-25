import apiClient from './axios';

export const categoryApi = {
  getAll: async () => {
    const response = await apiClient.get('/api/categories');
    return response.data;
  },

  getById: async (id) => {
    const response = await apiClient.get(`/api/categories/${id}`);
    return response.data;
  },

  create: async (categoryData) => {
    const response = await apiClient.post('/api/categories', categoryData);
    return response.data;
  },

  update: async (id, categoryData) => {
    const response = await apiClient.put(`/api/categories/${id}`, categoryData);
    return response.data;
  },

  delete: async (id) => {
    const response = await apiClient.delete(`/api/categories/${id}`);
    return response.data;
  },
};
