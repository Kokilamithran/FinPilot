import React, { useEffect, useState } from 'react';
import { Plus, Target, Trash2, Calendar, Coins, CheckCircle2, TrendingUp, CheckCircle } from 'lucide-react';
import { useGoalStore } from '../store/useGoalStore';
import { Card } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Modal } from '../components/common/Modal';
import { Input } from '../components/common/Input';
import { Select } from '../components/common/Select';
import { Badge } from '../components/common/Badge';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { EmptyState } from '../components/common/EmptyState';
import { ErrorAlert } from '../components/common/ErrorAlert';
import { formatCurrency, formatDate } from '../utils/formatters';
import { GOAL_STATUSES, GOAL_PRIORITIES } from '../utils/constants';

export const GoalsPage = () => {
  const { goals, contributions, isLoading, error, fetchGoals, fetchContributions, createGoal, createContribution, deleteGoal } =
    useGoalStore();

  const [isGoalModalOpen, setIsGoalModalOpen] = useState(false);
  const [isContribModalOpen, setIsContribModalOpen] = useState(false);
  const [selectedGoalId, setSelectedGoalId] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');

  const [goalForm, setGoalForm] = useState({
    goalName: '',
    goalCategory: 'Savings',
    targetAmount: '',
    currentAmount: '0',
    targetDate: new Date(new Date().setFullYear(new Date().getFullYear() + 1)).toISOString().split('T')[0],
    priority: 'MEDIUM',
    status: 'IN_PROGRESS',
  });

  const [contribForm, setContribForm] = useState({
    amount: '',
    notes: '',
  });

  const [formError, setFormError] = useState('');

  useEffect(() => {
    fetchGoals();
    fetchContributions();
  }, []);

  const handleCreateGoal = async (e) => {
    e.preventDefault();
    setFormError('');
    setSuccessMsg('');
    try {
      await createGoal({
        ...goalForm,
        targetAmount: parseFloat(goalForm.targetAmount) || 0,
        currentAmount: parseFloat(goalForm.currentAmount) || 0,
      });
      setIsGoalModalOpen(false);
      setSuccessMsg('Financial goal created successfully!');
      setTimeout(() => setSuccessMsg(''), 4000);
    } catch (err) {
      setFormError(err.response?.data?.message || err.message || 'Failed to create goal');
    }
  };

  const handleAddContribution = async (e) => {
    e.preventDefault();
    setFormError('');
    setSuccessMsg('');
    try {
      await createContribution({
        goalId: selectedGoalId,
        amount: parseFloat(contribForm.amount) || 0,
        contributionDate: new Date().toISOString(),
        notes: contribForm.notes,
      });
      fetchGoals(); // Refresh goal currentAmount
      setIsContribModalOpen(false);
      setSuccessMsg('Goal deposit recorded successfully!');
      setTimeout(() => setSuccessMsg(''), 4000);
    } catch (err) {
      setFormError(err.response?.data?.message || err.message || 'Failed to add contribution');
    }
  };

  const handleDeleteGoal = async (id) => {
    if (window.confirm('Delete this financial goal?')) {
      try {
        await deleteGoal(id);
        setSuccessMsg('Financial goal deleted successfully!');
        setTimeout(() => setSuccessMsg(''), 4000);
      } catch (err) {
        // Handled in store error state
      }
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Financial Goals</h1>
          <p className="text-xs text-slate-500 mt-1">Track long-term savings goals and target milestones</p>
        </div>
        <Button onClick={() => setIsGoalModalOpen(true)} icon={Plus}>
          New Financial Goal
        </Button>
      </div>

      {successMsg && (
        <div className="flex items-center gap-2 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
          <CheckCircle className="w-4 h-4" />
          <span>{successMsg}</span>
        </div>
      )}

      {error && <ErrorAlert message={error} />}

      {isLoading && goals.length === 0 ? (
        <LoadingSpinner label="Loading goals..." />
      ) : goals.length === 0 ? (
        <EmptyState
          icon={Target}
          title="No goals tracked yet"
          description="Create savings targets like Emergency Fund, Vacation, or Car Purchase."
          actionLabel="Create First Goal"
          onAction={() => setIsGoalModalOpen(true)}
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
          {goals.map((goal) => {
            const target = parseFloat(goal.targetAmount) || 0;
            const current = parseFloat(goal.currentAmount) || 0;
            const percent = target > 0 ? Math.min(100, Math.round((current / target) * 100)) : 0;
            const isAchieved = percent >= 100 || goal.status === 'ACHIEVED';

            return (
              <Card key={goal.goalId} className="space-y-4 relative">
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <div className={`p-3 rounded-xl border shadow-xs ${
                      isAchieved
                        ? 'bg-emerald-50 text-emerald-600 border-emerald-100'
                        : 'bg-blue-50 text-blue-600 border-blue-100'
                    }`}>
                      {isAchieved ? <CheckCircle2 className="w-6 h-6" /> : <Target className="w-6 h-6" />}
                    </div>
                    <div>
                      <h3 className="text-base font-bold text-slate-900">{goal.goalName}</h3>
                      <div className="flex items-center gap-1.5 mt-1">
                        <Badge variant={goal.priority === 'HIGH' ? 'rose' : 'amber'} size="sm">
                          {goal.priority} Priority
                        </Badge>
                        <Badge variant={isAchieved ? 'emerald' : 'sky'} size="sm">
                          {goal.status}
                        </Badge>
                      </div>
                    </div>
                  </div>

                  <div className="flex items-center gap-2">
                    {!isAchieved && (
                      <Button
                        size="sm"
                        variant="secondary"
                        onClick={() => {
                          setSelectedGoalId(goal.goalId);
                          setContribForm({ amount: '', notes: '' });
                          setIsContribModalOpen(true);
                        }}
                        icon={Coins}
                      >
                        Deposit
                      </Button>
                    )}
                    <button
                      onClick={() => handleDeleteGoal(goal.goalId)}
                      className="p-1.5 rounded-lg text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition-colors"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>

                <div className="space-y-1.5 pt-2">
                  <div className="flex items-center justify-between text-xs">
                    <span className="text-slate-600">Target Date: <strong className="text-slate-900 font-bold">{formatDate(goal.targetDate)}</strong></span>
                    <span className="font-extrabold text-slate-900">{percent}% Complete</span>
                  </div>

                  <div className="w-full h-3 bg-slate-100 rounded-full overflow-hidden p-0.5 border border-slate-200">
                    <div
                      className={`h-full rounded-full transition-all duration-500 ${
                        isAchieved ? 'bg-gradient-to-r from-emerald-500 to-teal-400' : 'bg-emerald-500'
                      }`}
                      style={{ width: `${percent}%` }}
                    />
                  </div>

                  <div className="flex items-center justify-between text-xs pt-1">
                    <span className="text-emerald-600 font-extrabold">{formatCurrency(current)} saved</span>
                    <span className="text-slate-600 font-medium">Target: {formatCurrency(target)}</span>
                  </div>
                </div>
              </Card>
            );
          })}
        </div>
      )}

      {/* Goal Modal */}
      <Modal
        isOpen={isGoalModalOpen}
        onClose={() => setIsGoalModalOpen(false)}
        title="Create Savings Goal"
        subtitle="Define milestone amount and deadline"
      >
        <ErrorAlert message={formError} onClose={() => setFormError('')} />
        <form onSubmit={handleCreateGoal} className="space-y-4">
          <Input
            label="Goal Name"
            value={goalForm.goalName}
            onChange={(e) => setGoalForm({ ...goalForm, goalName: e.target.value })}
            placeholder="e.g. Emergency Fund"
            required
          />

          <Input
            label="Target Amount (₹)"
            type="number"
            value={goalForm.targetAmount}
            onChange={(e) => setGoalForm({ ...goalForm, targetAmount: e.target.value })}
            placeholder="100000"
            required
          />

          <Input
            label="Current Starting Amount (₹)"
            type="number"
            value={goalForm.currentAmount}
            onChange={(e) => setGoalForm({ ...goalForm, currentAmount: e.target.value })}
            placeholder="0"
          />

          <Input
            label="Target Completion Date"
            type="date"
            value={goalForm.targetDate}
            onChange={(e) => setGoalForm({ ...goalForm, targetDate: e.target.value })}
            required
          />

          <Select
            label="Priority"
            value={goalForm.priority}
            onChange={(e) => setGoalForm({ ...goalForm, priority: e.target.value })}
            options={GOAL_PRIORITIES}
          />

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <Button type="button" variant="secondary" onClick={() => setIsGoalModalOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary" isLoading={isLoading}>
              Save Goal
            </Button>
          </div>
        </form>
      </Modal>

      {/* Contribution Deposit Modal */}
      <Modal
        isOpen={isContribModalOpen}
        onClose={() => setIsContribModalOpen(false)}
        title="Deposit Goal Contribution"
        subtitle="Log funds contributed toward this savings goal"
      >
        <ErrorAlert message={formError} onClose={() => setFormError('')} />
        <form onSubmit={handleAddContribution} className="space-y-4">
          <Input
            label="Contribution Amount (₹)"
            type="number"
            value={contribForm.amount}
            onChange={(e) => setContribForm({ ...contribForm, amount: e.target.value })}
            placeholder="5000"
            required
          />

          <Input
            label="Notes"
            value={contribForm.notes}
            onChange={(e) => setContribForm({ ...contribForm, notes: e.target.value })}
            placeholder="e.g. Monthly savings transfer"
          />

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <Button type="button" variant="secondary" onClick={() => setIsContribModalOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary" isLoading={isLoading}>
              Add Contribution
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
