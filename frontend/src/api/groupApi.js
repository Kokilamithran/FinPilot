import apiClient from './axios';

export const groupApi = {
  // Groups
  getAllGroups: async () => {
    const response = await apiClient.get('/api/groups');
    return response.data;
  },

  createGroup: async (groupData) => {
    const response = await apiClient.post('/api/groups', groupData);
    return response.data;
  },

  deleteGroup: async (id) => {
    const response = await apiClient.delete(`/api/groups/${id}`);
    return response.data;
  },

  // Members
  getAllMembers: async () => {
    const response = await apiClient.get('/api/group-members');
    return response.data;
  },

  createMember: async (memberData) => {
    const response = await apiClient.post('/api/group-members', memberData);
    return response.data;
  },

  // Expenses
  getAllGroupExpenses: async () => {
    const response = await apiClient.get('/api/group-expenses');
    return response.data;
  },

  createGroupExpense: async (expenseData) => {
    const response = await apiClient.post('/api/group-expenses', expenseData);
    return response.data;
  },

  // Shares
  getAllGroupExpenseShares: async () => {
    const response = await apiClient.get('/api/group-expense-shares');
    return response.data;
  },

  createGroupExpenseShare: async (shareData) => {
    const response = await apiClient.post('/api/group-expense-shares', shareData);
    return response.data;
  },
};
