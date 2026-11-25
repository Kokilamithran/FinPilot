import React, { useEffect, useState } from 'react';
import { Plus, Wallet, Trash2, Edit2, CreditCard, ShieldCheck, CheckCircle } from 'lucide-react';
import { useAccountStore } from '../store/useAccountStore';
import { Card } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Modal } from '../components/common/Modal';
import { Input } from '../components/common/Input';
import { Select } from '../components/common/Select';
import { Badge } from '../components/common/Badge';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { EmptyState } from '../components/common/EmptyState';
import { ErrorAlert } from '../components/common/ErrorAlert';
import { formatCurrency } from '../utils/formatters';
import { ACCOUNT_TYPES } from '../utils/constants';

export const AccountsPage = () => {
  const { accounts, isLoading, error, fetchAccounts, createAccount, updateAccount, deleteAccount } =
    useAccountStore();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    accountName: '',
    accountType: 'SAVINGS',
    currency: 'INR',
    currentBalance: '0',
    creditLimit: '0',
    isActive: true,
  });
  const [formError, setFormError] = useState('');

  useEffect(() => {
    fetchAccounts();
  }, []);

  const totalBalance = accounts.reduce((acc, a) => acc + (parseFloat(a.currentBalance) || 0), 0);

  const handleOpenModal = (account = null) => {
    setFormError('');
    if (account) {
      setEditingId(account.accountId);
      setFormData({
        accountName: account.accountName || '',
        accountType: account.accountType || 'SAVINGS',
        currency: account.currency || 'INR',
        currentBalance: account.currentBalance !== undefined ? String(account.currentBalance) : '0',
        creditLimit: account.creditLimit !== undefined ? String(account.creditLimit) : '0',
        isActive: account.isActive !== undefined ? account.isActive : true,
      });
    } else {
      setEditingId(null);
      setFormData({
        accountName: '',
        accountType: 'SAVINGS',
        currency: 'INR',
        currentBalance: '0',
        creditLimit: '0',
        isActive: true,
      });
    }
    setIsModalOpen(true);
  };

  const [successMsg, setSuccessMsg] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setFormError('');
    setSuccessMsg('');

    try {
      const payload = {
        ...formData,
        currentBalance: parseFloat(formData.currentBalance) || 0,
        creditLimit: parseFloat(formData.creditLimit) || 0,
      };

      if (editingId) {
        await updateAccount(editingId, payload);
        setSuccessMsg('Account updated successfully!');
      } else {
        await createAccount(payload);
        setSuccessMsg('Account created successfully!');
      }
      setIsModalOpen(false);
      setTimeout(() => setSuccessMsg(''), 4000);
    } catch (err) {
      setFormError(err.message || 'Failed to save account');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this account?')) {
      try {
        await deleteAccount(id);
        setSuccessMsg('Account deleted successfully!');
        setTimeout(() => setSuccessMsg(''), 4000);
      } catch (err) {
        // Handled in store error state
      }
    }
  };

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Financial Accounts</h1>
          <p className="text-xs text-slate-500 mt-1">Manage your bank accounts, wallets, and credit lines</p>
        </div>
        <Button onClick={() => handleOpenModal()} icon={Plus}>
          Add Account
        </Button>
      </div>

      {successMsg && (
        <div className="flex items-center gap-2 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
          <CheckCircle className="w-4 h-4" />
          <span>{successMsg}</span>
        </div>
      )}

      {error && <ErrorAlert message={error} />}

      {/* Summary Banner */}
      <Card className="border-slate-200 shadow-xs">
        <div className="flex items-center justify-between">
          <div>
            <span className="text-xs text-slate-500 font-semibold uppercase tracking-wider">Total Net Assets</span>
            <h2 className="text-3xl font-extrabold text-slate-900 mt-1">
              {formatCurrency(totalBalance)}
            </h2>
          </div>
          <div className="p-3 rounded-2xl bg-emerald-50 text-emerald-600 border border-emerald-100 shadow-xs">
            <Wallet className="w-8 h-8" />
          </div>
        </div>
      </Card>

      {/* Accounts List Grid */}
      {isLoading && accounts.length === 0 ? (
        <LoadingSpinner label="Loading accounts..." />
      ) : accounts.length === 0 ? (
        <EmptyState
          icon={Wallet}
          title="No accounts created yet"
          description="Create your checking, savings, or investment account to track balances."
          actionLabel="Add First Account"
          onAction={() => handleOpenModal()}
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {accounts.map((account) => (
            <Card key={account.accountId} hoverable className="relative group">
              <div className="flex items-start justify-between">
                <div className="flex items-center gap-3">
                  <div className="p-3 rounded-xl bg-slate-100 text-slate-700 border border-slate-200 shadow-xs">
                    <CreditCard className="w-5 h-5" />
                  </div>
                  <div>
                    <h3 className="text-base font-bold text-slate-900">{account.accountName}</h3>
                    <div className="flex items-center gap-2 mt-1">
                      <Badge variant="indigo" size="sm">{account.accountType}</Badge>
                      {account.isActive ? (
                        <Badge variant="emerald" size="sm">Active</Badge>
                      ) : (
                        <Badge variant="neutral" size="sm">Inactive</Badge>
                      )}
                    </div>
                  </div>
                </div>
                <div className="flex items-center gap-1 opacity-80 group-hover:opacity-100 transition-opacity">
                  <button
                    onClick={() => handleOpenModal(account)}
                    className="p-1.5 rounded-lg text-slate-400 hover:text-slate-900 hover:bg-slate-100 transition-colors"
                  >
                    <Edit2 className="w-4 h-4" />
                  </button>
                  <button
                    onClick={() => handleDelete(account.accountId)}
                    className="p-1.5 rounded-lg text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition-colors"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>

              <div className="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between">
                <span className="text-xs text-slate-500 font-medium">Current Balance</span>
                <span className="text-xl font-extrabold text-emerald-600">
                  {formatCurrency(account.currentBalance, account.currency)}
                </span>
              </div>
            </Card>
          ))}
        </div>
      )}

      {/* Modal Form */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingId ? 'Edit Account' : 'Create New Account'}
        subtitle="Specify account details and current starting balance"
      >
        <ErrorAlert message={formError} onClose={() => setFormError('')} />
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Account Name"
            value={formData.accountName}
            onChange={(e) => setFormData({ ...formData, accountName: e.target.value })}
            placeholder="e.g. HDFC Salary Account"
            required
          />

          <Select
            label="Account Type"
            value={formData.accountType}
            onChange={(e) => setFormData({ ...formData, accountType: e.target.value })}
            options={ACCOUNT_TYPES}
            required
          />

          <Input
            label="Current Balance (₹)"
            type="number"
            step="0.01"
            value={formData.currentBalance}
            onChange={(e) => setFormData({ ...formData, currentBalance: e.target.value })}
            required
          />

          <Input
            label="Credit Limit (Optional)"
            type="number"
            step="0.01"
            value={formData.creditLimit}
            onChange={(e) => setFormData({ ...formData, creditLimit: e.target.value })}
          />

          <div className="flex items-center gap-2 pt-2">
            <input
              type="checkbox"
              id="isActive"
              checked={formData.isActive}
              onChange={(e) => setFormData({ ...formData, isActive: e.target.checked })}
              className="w-4 h-4 rounded bg-white border-slate-300 text-emerald-600 focus:ring-emerald-500"
            />
            <label htmlFor="isActive" className="text-xs text-slate-700">
              Account is active
            </label>
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <Button type="button" variant="secondary" onClick={() => setIsModalOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary" isLoading={isLoading}>
              {editingId ? 'Save Changes' : 'Create Account'}
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
