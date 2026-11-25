import apiClient from './axios';

export const goalApi = {
  // Goals
  getAllGoals: async () => {
    const response = await apiClient.get('/api/goals');
    return response.data;
  },

  createGoal: async (goalData) => {
    const response = await apiClient.post('/api/goals', goalData);
    return response.data;
  },

  updateGoal: async (id, goalData) => {
    const response = await apiClient.put(`/api/goals/${id}`, goalData);
    return response.data;
  },

  deleteGoal: async (id) => {
    const response = await apiClient.delete(`/api/goals/${id}`);
    return response.data;
  },

  // Contributions
  getAllContributions: async () => {
    const response = await apiClient.get('/api/goal-contributions');
    return response.data;
  },

  createContribution: async (contribData) => {
    const response = await apiClient.post('/api/goal-contributions', contribData);
    return response.data;
  },

  deleteContribution: async (id) => {
    const response = await apiClient.delete(`/api/goal-contributions/${id}`);
    return response.data;
  },
};
