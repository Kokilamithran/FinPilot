import apiClient from './axios';

export const notificationApi = {
  getAll: async () => {
    const response = await apiClient.get('/api/notifications');
    return response.data;
  },

  create: async (data) => {
    const response = await apiClient.post('/api/notifications', data);
    return response.data;
  },

  update: async (id, data) => {
    const response = await apiClient.put(`/api/notifications/${id}`, data);
    return response.data;
  },

  delete: async (id) => {
    const response = await apiClient.delete(`/api/notifications/${id}`);
    return response.data;
  },
};
