import React, { useEffect, useState } from 'react';
import { Plus, Users, Trash2, DollarSign, SplitSquareVertical, CheckCircle } from 'lucide-react';
import { useGroupStore } from '../store/useGroupStore';
import { Card } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Modal } from '../components/common/Modal';
import { Input } from '../components/common/Input';
import { Badge } from '../components/common/Badge';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { EmptyState } from '../components/common/EmptyState';
import { ErrorAlert } from '../components/common/ErrorAlert';
import { formatCurrency, formatDate } from '../utils/formatters';

export const GroupsPage = () => {
  const { groups, groupExpenses, isLoading, error, fetchGroups, fetchGroupExpenses, createGroup, createGroupExpense } =
    useGroupStore();

  const [isGroupModalOpen, setIsGroupModalOpen] = useState(false);
  const [groupForm, setGroupForm] = useState({
    groupName: '',
    groupType: 'TRIP',
    description: '',
    isActive: true,
  });

  const [formError, setFormError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  useEffect(() => {
    fetchGroups();
    fetchGroupExpenses();
  }, []);

  const handleCreateGroup = async (e) => {
    e.preventDefault();
    setFormError('');
    setSuccessMsg('');
    try {
      await createGroup(groupForm);
      setIsGroupModalOpen(false);
      setSuccessMsg('Group created successfully!');
    } catch (err) {
      setFormError(err.message || 'Failed to create group');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Shared Expense Groups</h1>
          <p className="text-xs text-slate-500 mt-1">Split bills and manage group trip or household expenses</p>
        </div>
        <Button onClick={() => setIsGroupModalOpen(true)} icon={Plus}>
          Create Group
        </Button>
      </div>

      {successMsg && (
        <div className="flex items-center gap-2 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
          <CheckCircle className="w-4 h-4" />
          <span>{successMsg}</span>
        </div>
      )}

      {error && <ErrorAlert message={error} />}

      {isLoading && groups.length === 0 ? (
        <LoadingSpinner label="Loading expense groups..." />
      ) : groups.length === 0 ? (
        <EmptyState
          icon={Users}
          title="No expense groups created"
          description="Create a group for trips, roommates, or shared events to split expenses."
          actionLabel="Create Group"
          onAction={() => setIsGroupModalOpen(true)}
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {groups.map((group) => {
            const expenses = groupExpenses.filter((e) => e.groupId === group.groupId);
            const totalGroupSpend = expenses.reduce((acc, e) => acc + (parseFloat(e.totalAmount) || 0), 0);

            return (
              <Card key={group.groupId} hoverable className="space-y-4">
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <div className="p-3 rounded-xl bg-purple-50 text-purple-600 border border-purple-100 shadow-xs">
                      <Users className="w-6 h-6" />
                    </div>
                    <div>
                      <h3 className="text-base font-bold text-slate-900">{group.groupName}</h3>
                      <p className="text-xs text-slate-500 mt-0.5">{group.description || 'Shared expense group'}</p>
                    </div>
                  </div>
                  <Badge variant="indigo" size="sm">{group.groupType}</Badge>
                </div>

                <div className="pt-3 border-t border-slate-100 flex justify-between items-center text-xs">
                  <span className="text-slate-600 font-medium">Total Expenses</span>
                  <span className="font-extrabold text-slate-900">{formatCurrency(totalGroupSpend)}</span>
                </div>
              </Card>
            );
          })}
        </div>
      )}

      {/* Modal */}
      <Modal
        isOpen={isGroupModalOpen}
        onClose={() => setIsGroupModalOpen(false)}
        title="Create Expense Group"
        subtitle="Organize shared bill splits"
      >
        <ErrorAlert message={formError} onClose={() => setFormError('')} />
        <form onSubmit={handleCreateGroup} className="space-y-4">
          <Input
            label="Group Name"
            value={groupForm.groupName}
            onChange={(e) => setGroupForm({ ...groupForm, groupName: e.target.value })}
            placeholder="e.g. Goa Trip 2026, Apartment 4B"
            required
          />

          <Input
            label="Group Type"
            value={groupForm.groupType}
            onChange={(e) => setGroupForm({ ...groupForm, groupType: e.target.value })}
            placeholder="e.g. TRIP, HOUSEHOLD, EVENT"
            required
          />

          <Input
            label="Description"
            value={groupForm.description}
            onChange={(e) => setGroupForm({ ...groupForm, description: e.target.value })}
            placeholder="Optional group description..."
          />

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <Button type="button" variant="secondary" onClick={() => setIsGroupModalOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary" isLoading={isLoading}>
              Save Group
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
