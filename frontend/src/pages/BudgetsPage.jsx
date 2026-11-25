import React, { useEffect, useState } from 'react';
import { Plus, PieChart, Trash2, Calendar, Target, AlertTriangle, CheckCircle } from 'lucide-react';
import { useBudgetStore } from '../store/useBudgetStore';
import { useCategoryStore } from '../store/useCategoryStore';
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
import { PERIOD_TYPES } from '../utils/constants';

export const BudgetsPage = () => {
  const { budgets, budgetItems, isLoading, error, fetchBudgets, fetchBudgetItems, createBudget, createBudgetItem, deleteBudget } =
    useBudgetStore();
  const { categories, fetchCategories } = useCategoryStore();

  const [isBudgetModalOpen, setIsBudgetModalOpen] = useState(false);
  const [isItemModalOpen, setIsItemModalOpen] = useState(false);
  const [selectedBudgetId, setSelectedBudgetId] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');

  const [budgetForm, setBudgetForm] = useState({
    budgetName: '',
    periodType: 'MONTHLY',
    startDate: new Date().toISOString().split('T')[0],
    endDate: new Date(new Date().setMonth(new Date().getMonth() + 1)).toISOString().split('T')[0],
    totalAmount: '',
    currency: 'INR',
    isActive: true,
  });

  const [itemForm, setItemForm] = useState({
    categoryId: '',
    allocatedAmount: '',
    notes: '',
  });

  const [formError, setFormError] = useState('');

  useEffect(() => {
    fetchBudgets();
    fetchBudgetItems();
    fetchCategories();
  }, []);

  const handleCreateBudget = async (e) => {
    e.preventDefault();
    setFormError('');
    setSuccessMsg('');
    try {
      await createBudget({
        ...budgetForm,
        totalAmount: parseFloat(budgetForm.totalAmount) || 0,
      });
      setIsBudgetModalOpen(false);
      setSuccessMsg('Budget plan created successfully!');
      setTimeout(() => setSuccessMsg(''), 4000);
    } catch (err) {
      setFormError(err.response?.data?.message || err.message || 'Failed to create budget');
    }
  };

  const handleCreateItem = async (e) => {
    e.preventDefault();
    setFormError('');
    setSuccessMsg('');
    try {
      await createBudgetItem({
        budgetId: selectedBudgetId,
        categoryId: parseInt(itemForm.categoryId, 10),
        allocatedAmount: parseFloat(itemForm.allocatedAmount) || 0,
        spentAmount: 0,
        notes: itemForm.notes,
      });
      setIsItemModalOpen(false);
      setSuccessMsg('Category allocation added successfully!');
      setTimeout(() => setSuccessMsg(''), 4000);
    } catch (err) {
      setFormError(err.response?.data?.message || err.message || 'Failed to allocate budget item');
    }
  };

  const handleDeleteBudget = async (id) => {
    if (window.confirm('Delete this budget target?')) {
      try {
        await deleteBudget(id);
        setSuccessMsg('Budget deleted successfully!');
        setTimeout(() => setSuccessMsg(''), 4000);
      } catch (err) {
        // Handled in store error state
      }
    }
  };

  const categoryMap = categories.reduce((acc, c) => ({ ...acc, [c.categoryId]: c.categoryName }), {});

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Budgeting & Allocation</h1>
          <p className="text-xs text-slate-500 mt-1">Set monthly limit goals and category caps</p>
        </div>
        <Button onClick={() => setIsBudgetModalOpen(true)} icon={Plus}>
          Create Budget
        </Button>
      </div>

      {successMsg && (
        <div className="flex items-center gap-2 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
          <CheckCircle className="w-4 h-4" />
          <span>{successMsg}</span>
        </div>
      )}

      {error && <ErrorAlert message={error} />}

      {isLoading && budgets.length === 0 ? (
        <LoadingSpinner label="Loading budgets..." />
      ) : budgets.length === 0 ? (
        <EmptyState
          icon={PieChart}
          title="No budget plans configured"
          description="Create your first budget to monitor category allocations and spending boundaries."
          actionLabel="Create Budget"
          onAction={() => setIsBudgetModalOpen(true)}
        />
      ) : (
        <div className="space-y-6">
          {budgets.map((budget) => {
            const items = budgetItems.filter((item) => item.budgetId === budget.budgetId);
            const totalAllocated = items.reduce((acc, item) => acc + (parseFloat(item.allocatedAmount) || 0), 0);
            const totalSpent = items.reduce((acc, item) => acc + (parseFloat(item.spentAmount) || 0), 0);
            const budgetCap = parseFloat(budget.totalAmount) || 0;
            const progressPercent = budgetCap > 0 ? Math.min(100, Math.round((totalSpent / budgetCap) * 100)) : 0;

            return (
              <Card key={budget.budgetId} className="space-y-4">
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <div className="p-3 rounded-xl bg-emerald-50 text-emerald-600 border border-emerald-100 shadow-xs">
                      <PieChart className="w-6 h-6" />
                    </div>
                    <div>
                      <div className="flex items-center gap-2">
                        <h3 className="text-lg font-bold text-slate-900">{budget.budgetName}</h3>
                        <Badge variant="indigo" size="sm">{budget.periodType}</Badge>
                      </div>
                      <p className="text-xs text-slate-500 mt-0.5">
                        {formatDate(budget.startDate)} - {formatDate(budget.endDate)}
                      </p>
                    </div>
                  </div>

                  <div className="flex items-center gap-2">
                    <Button
                      size="sm"
                      variant="secondary"
                      onClick={() => {
                        setSelectedBudgetId(budget.budgetId);
                        setItemForm({ categoryId: categories[0]?.categoryId ? String(categories[0].categoryId) : '', allocatedAmount: '', notes: '' });
                        setIsItemModalOpen(true);
                      }}
                      icon={Plus}
                    >
                      Allocate Category
                    </Button>
                    <button
                      onClick={() => handleDeleteBudget(budget.budgetId)}
                      className="p-2 rounded-xl text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition-colors"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>

                {/* Progress bar */}
                <div className="space-y-1.5 pt-2">
                  <div className="flex items-center justify-between text-xs">
                    <span className="text-slate-600 font-medium">Spent {formatCurrency(totalSpent)} of {formatCurrency(budgetCap)}</span>
                    <span className="font-extrabold text-slate-900">{progressPercent}%</span>
                  </div>
                  <div className="w-full h-2.5 bg-slate-100 border border-slate-200 rounded-full overflow-hidden">
                    <div
                      className={`h-full rounded-full transition-all duration-500 ${
                        progressPercent > 90 ? 'bg-rose-500' : progressPercent > 75 ? 'bg-amber-500' : 'bg-emerald-500'
                      }`}
                      style={{ width: `${progressPercent}%` }}
                    />
                  </div>
                </div>

                {/* Budget Items Table */}
                {items.length > 0 && (
                  <div className="mt-4 pt-4 border-t border-slate-100">
                    <h4 className="text-[11px] font-bold text-slate-600 uppercase tracking-wider mb-3">Allocated Categories</h4>
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-3">
                      {items.map((item) => (
                        <div key={item.itemId || item.budgetItemId} className="p-3.5 rounded-xl bg-slate-50 border border-slate-200/80 flex justify-between items-center shadow-xs">
                          <div>
                            <div className="text-xs font-bold text-slate-900">
                              {categoryMap[item.categoryId] || `Category #${item.categoryId}`}
                            </div>
                            <div className="text-[11px] text-slate-500 mt-0.5">
                              Spent {formatCurrency(item.spentAmount)}
                            </div>
                          </div>
                          <div className="text-right">
                            <span className="text-xs font-extrabold text-emerald-600">
                              {formatCurrency(item.allocatedAmount)}
                            </span>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                )}
              </Card>
            );
          })}
        </div>
      )}

      {/* Create Budget Modal */}
      <Modal
        isOpen={isBudgetModalOpen}
        onClose={() => setIsBudgetModalOpen(false)}
        title="Create New Budget"
        subtitle="Establish overall spending boundaries"
      >
        <ErrorAlert message={formError} onClose={() => setFormError('')} />
        <form onSubmit={handleCreateBudget} className="space-y-4">
          <Input
            label="Budget Title"
            value={budgetForm.budgetName}
            onChange={(e) => setBudgetForm({ ...budgetForm, budgetName: e.target.value })}
            placeholder="e.g. Monthly Living Expenses"
            required
          />

          <Select
            label="Period Type"
            value={budgetForm.periodType}
            onChange={(e) => setBudgetForm({ ...budgetForm, periodType: e.target.value })}
            options={PERIOD_TYPES}
            required
          />

          <Input
            label="Total Amount Cap (₹)"
            type="number"
            value={budgetForm.totalAmount}
            onChange={(e) => setBudgetForm({ ...budgetForm, totalAmount: e.target.value })}
            placeholder="50000"
            required
          />

          <div className="grid grid-cols-2 gap-3">
            <Input
              label="Start Date"
              type="date"
              value={budgetForm.startDate}
              onChange={(e) => setBudgetForm({ ...budgetForm, startDate: e.target.value })}
              required
            />
            <Input
              label="End Date"
              type="date"
              value={budgetForm.endDate}
              onChange={(e) => setBudgetForm({ ...budgetForm, endDate: e.target.value })}
              required
            />
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <Button type="button" variant="secondary" onClick={() => setIsBudgetModalOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary" isLoading={isLoading}>
              Create Budget
            </Button>
          </div>
        </form>
      </Modal>

      {/* Create Budget Item Modal */}
      <Modal
        isOpen={isItemModalOpen}
        onClose={() => setIsItemModalOpen(false)}
        title="Allocate Category Limit"
        subtitle="Assign a specific cap to a category within this budget"
      >
        <ErrorAlert message={formError} onClose={() => setFormError('')} />
        <form onSubmit={handleCreateItem} className="space-y-4">
          <Select
            label="Category"
            value={itemForm.categoryId}
            onChange={(e) => setItemForm({ ...itemForm, categoryId: e.target.value })}
            options={categories.map((c) => ({ value: String(c.categoryId), label: c.categoryName }))}
            required
          />

          <Input
            label="Allocated Cap (₹)"
            type="number"
            value={itemForm.allocatedAmount}
            onChange={(e) => setItemForm({ ...itemForm, allocatedAmount: e.target.value })}
            placeholder="10000"
            required
          />

          <Input
            label="Notes"
            value={itemForm.notes}
            onChange={(e) => setItemForm({ ...itemForm, notes: e.target.value })}
            placeholder="Optional allocation notes..."
          />

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <Button type="button" variant="secondary" onClick={() => setIsItemModalOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary" isLoading={isLoading}>
              Add Allocation
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
