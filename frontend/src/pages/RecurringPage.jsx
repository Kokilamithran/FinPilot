import React, { useEffect, useState } from 'react';
import { Plus, Repeat, Trash2, Calendar, CheckCircle } from 'lucide-react';
import { recurringApi } from '../api/recurringApi';
import { useAccountStore } from '../store/useAccountStore';
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

export const RecurringPage = () => {
  const [recurringList, setRecurringList] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);

  const { accounts, fetchAccounts } = useAccountStore();
  const { categories, fetchCategories } = useCategoryStore();

  const [formData, setFormData] = useState({
    name: '',
    transactionType: 'EXPENSE',
    amount: '',
    merchant: '',
    frequency: 'MONTHLY',
    nextDueDate: new Date().toISOString().split('T')[0],
    accountId: '',
    categoryId: '',
  });

  const loadData = async () => {
    setIsLoading(true);
    setError('');
    try {
      const data = await recurringApi.getAll();
      setRecurringList(data);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to load recurring transactions');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadData();
    fetchAccounts();
    fetchCategories();
  }, []);

  useEffect(() => {
    if (accounts.length > 0 && !formData.accountId) {
      setFormData((prev) => ({ ...prev, accountId: String(accounts[0].accountId) }));
    }
  }, [accounts]);

  useEffect(() => {
    if (categories.length > 0 && !formData.categoryId) {
      setFormData((prev) => ({ ...prev, categoryId: String(categories[0].categoryId) }));
    }
  }, [categories]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMsg('');

    if (!formData.accountId) {
      setError('Please select an account.');
      return;
    }
    if (!formData.categoryId) {
      setError('Please select a category.');
      return;
    }

    try {
      await recurringApi.create({
        name: formData.name,
        transactionType: formData.transactionType,
        amount: parseFloat(formData.amount) || 0,
        merchant: formData.merchant,
        frequency: formData.frequency,
        nextDueDate: formData.nextDueDate,
        accountId: parseInt(formData.accountId, 10),
        categoryId: parseInt(formData.categoryId, 10),
        startDate: new Date().toISOString().split('T')[0],
        isActive: true,
        autoLog: true,
      });
      setIsModalOpen(false);
      setSuccessMsg('Recurring rule added successfully!');
      setTimeout(() => setSuccessMsg(''), 4000);
      loadData();
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to add recurring rule');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Delete this recurring rule?')) {
      try {
        await recurringApi.delete(id);
        setSuccessMsg('Recurring rule deleted successfully!');
        setTimeout(() => setSuccessMsg(''), 4000);
        loadData();
      } catch (err) {
        setError(err.response?.data?.message || err.message || 'Failed to delete recurring rule');
      }
    }
  };

  const accountOptions = accounts.map((a) => ({ value: String(a.accountId), label: a.accountName }));
  const categoryOptions = categories.map((c) => ({ value: String(c.categoryId), label: c.categoryName }));

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Recurring Transactions</h1>
          <p className="text-xs text-slate-500 mt-1">Automate subscriptions, rent, and recurring bill logging</p>
        </div>
        <Button onClick={() => setIsModalOpen(true)} icon={Plus}>
          Add Recurring Rule
        </Button>
      </div>

      {successMsg && (
        <div className="flex items-center gap-2 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
          <CheckCircle className="w-4 h-4" />
          <span>{successMsg}</span>
        </div>
      )}

      {error && <ErrorAlert message={error} />}

      {isLoading && recurringList.length === 0 ? (
        <LoadingSpinner label="Loading schedules..." />
      ) : recurringList.length === 0 ? (
        <EmptyState
          icon={Repeat}
          title="No recurring transactions set"
          description="Schedule Netflix, Rent, Electricity, or SIP investments."
          actionLabel="Add First Rule"
          onAction={() => setIsModalOpen(true)}
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {recurringList.map((item) => (
            <Card key={item.recurringId || item.id} hoverable className="space-y-3">
              <div className="flex items-start justify-between">
                <div>
                  <h3 className="text-base font-bold text-slate-900">{item.name}</h3>
                  <div className="flex items-center gap-2 mt-1">
                    <Badge variant="indigo" size="sm">{item.frequency}</Badge>
                    <Badge variant={item.transactionType === 'INCOME' ? 'emerald' : 'rose'} size="sm">
                      {item.transactionType}
                    </Badge>
                  </div>
                </div>
                <button
                  onClick={() => handleDelete(item.recurringId || item.id)}
                  className="p-1.5 rounded-lg text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition-colors"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>

              <div className="pt-3 border-t border-slate-100 flex justify-between items-center text-xs">
                <span className="text-slate-600 font-medium">Due {formatDate(item.nextDueDate)}</span>
                <span className="text-base font-extrabold text-slate-900">
                  {formatCurrency(item.amount)}
                </span>
              </div>
            </Card>
          ))}
        </div>
      )}

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Schedule Recurring Rule"
        subtitle="Automated payment reminders and logging"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Rule Name"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            placeholder="e.g. Netflix Subscription, House Rent"
            required
          />

          <Select
            label="Transaction Type"
            value={formData.transactionType}
            onChange={(e) => setFormData({ ...formData, transactionType: e.target.value })}
            options={[
              { value: 'EXPENSE', label: 'Expense' },
              { value: 'INCOME', label: 'Income' },
            ]}
            required
          />

          <Select
            label="Account"
            value={formData.accountId}
            onChange={(e) => setFormData({ ...formData, accountId: e.target.value })}
            options={accountOptions}
            placeholder="Select Account"
            required
          />

          <Select
            label="Category"
            value={formData.categoryId}
            onChange={(e) => setFormData({ ...formData, categoryId: e.target.value })}
            options={categoryOptions}
            placeholder="Select Category"
            required
          />

          <Input
            label="Amount (₹)"
            type="number"
            min="0.01"
            step="any"
            value={formData.amount}
            onChange={(e) => setFormData({ ...formData, amount: e.target.value })}
            placeholder="649"
            required
          />

          <Select
            label="Frequency"
            value={formData.frequency}
            onChange={(e) => setFormData({ ...formData, frequency: e.target.value })}
            options={[
              { value: 'DAILY', label: 'Daily' },
              { value: 'WEEKLY', label: 'Weekly' },
              { value: 'MONTHLY', label: 'Monthly' },
              { value: 'YEARLY', label: 'Yearly' },
            ]}
            required
          />

          <Input
            label="Merchant / Payee"
            value={formData.merchant}
            onChange={(e) => setFormData({ ...formData, merchant: e.target.value })}
            placeholder="e.g. Netflix"
          />

          <Input
            label="Next Due Date"
            type="date"
            value={formData.nextDueDate}
            onChange={(e) => setFormData({ ...formData, nextDueDate: e.target.value })}
            required
          />

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <Button type="button" variant="secondary" onClick={() => setIsModalOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Save Schedule
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
