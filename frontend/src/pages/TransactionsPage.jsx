import React, { useEffect, useState } from 'react';
import { Plus, ArrowLeftRight, Search, Filter, Trash2, Edit2, ArrowDownRight, ArrowUpRight, CheckCircle } from 'lucide-react';
import { useTransactionStore } from '../store/useTransactionStore';
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
import { TRANSACTION_TYPES } from '../utils/constants';

export const TransactionsPage = () => {
  const { transactions, isLoading, error, fetchTransactions, createTransaction, updateTransaction, deleteTransaction } =
    useTransactionStore();
  const { accounts, fetchAccounts } = useAccountStore();
  const { categories, fetchCategories } = useCategoryStore();

  const [searchTerm, setSearchTerm] = useState('');
  const [selectedType, setSelectedType] = useState('');
  const [selectedAccountId, setSelectedAccountId] = useState('');

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');
  const [formData, setFormData] = useState({
    transactionType: 'EXPENSE',
    amount: '',
    currency: 'INR',
    transactionDate: new Date().toISOString().split('T')[0],
    merchant: '',
    notes: '',
    accountId: '',
    categoryId: '',
    isRecurring: false,
    isAutoImported: false,
  });

  const [formError, setFormError] = useState('');

  useEffect(() => {
    fetchTransactions();
    fetchAccounts();
    fetchCategories();
  }, []);

  // Filtering transactions client-side
  const filteredTransactions = transactions.filter((tx) => {
    const matchesSearch =
      !searchTerm ||
      (tx.merchant && tx.merchant.toLowerCase().includes(searchTerm.toLowerCase())) ||
      (tx.notes && tx.notes.toLowerCase().includes(searchTerm.toLowerCase()));
    const matchesType = !selectedType || tx.transactionType === selectedType;
    const matchesAccount = !selectedAccountId || String(tx.accountId) === String(selectedAccountId);
    return matchesSearch && matchesType && matchesAccount;
  });

  const handleOpenModal = (tx = null) => {
    setFormError('');
    if (tx) {
      setEditingId(tx.transactionId);
      setFormData({
        transactionType: tx.transactionType || 'EXPENSE',
        amount: tx.amount !== undefined ? String(tx.amount) : '',
        currency: tx.currency || 'INR',
        transactionDate: tx.transactionDate ? tx.transactionDate.split('T')[0] : new Date().toISOString().split('T')[0],
        merchant: tx.merchant || '',
        notes: tx.notes || '',
        accountId: tx.accountId ? String(tx.accountId) : (accounts[0]?.accountId ? String(accounts[0].accountId) : ''),
        categoryId: tx.categoryId ? String(tx.categoryId) : '',
        isRecurring: tx.isRecurring ?? false,
        isAutoImported: tx.isAutoImported ?? false,
      });
    } else {
      setEditingId(null);
      setFormData({
        transactionType: 'EXPENSE',
        amount: '',
        currency: 'INR',
        transactionDate: new Date().toISOString().split('T')[0],
        merchant: '',
        notes: '',
        accountId: accounts[0]?.accountId ? String(accounts[0].accountId) : '',
        categoryId: '',
        isRecurring: false,
        isAutoImported: false,
      });
    }
    setIsModalOpen(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setFormError('');
    setSuccessMsg('');

    if (!formData.accountId) {
      setFormError('Please select a valid account');
      return;
    }

    const numAmount = parseFloat(formData.amount);
    if (isNaN(numAmount) || numAmount <= 0) {
      setFormError('Transaction amount must be positive');
      return;
    }

    try {
      const payload = {
        transactionType: formData.transactionType,
        amount: numAmount,
        baseCurrencyAmount: numAmount,
        currency: formData.currency,
        exchangeRate: 1.0,
        transactionDate: `${formData.transactionDate}T12:00:00`,
        merchant: formData.merchant,
        notes: formData.notes,
        accountId: parseInt(formData.accountId, 10),
        categoryId: formData.categoryId ? parseInt(formData.categoryId, 10) : null,
        isRecurring: formData.isRecurring,
        isAutoImported: formData.isAutoImported,
      };

      if (editingId) {
        await updateTransaction(editingId, payload);
        setSuccessMsg('Transaction updated successfully!');
      } else {
        await createTransaction(payload);
        setSuccessMsg('Transaction created successfully!');
      }
      setIsModalOpen(false);
      fetchAccounts();
      setTimeout(() => setSuccessMsg(''), 4000);
    } catch (err) {
      setFormError(err.response?.data?.message || err.message || 'Failed to save transaction');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this transaction?')) {
      try {
        await deleteTransaction(id);
        setSuccessMsg('Transaction deleted successfully!');
        fetchAccounts();
        setTimeout(() => setSuccessMsg(''), 4000);
      } catch (err) {
        // Error state handled in store/page
      }
    }
  };

  const accountMap = accounts.reduce((acc, a) => ({ ...acc, [a.accountId]: a.accountName }), {});
  const categoryMap = categories.reduce((acc, c) => ({ ...acc, [c.categoryId]: c.categoryName }), {});

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Transactions</h1>
          <p className="text-xs text-slate-500 mt-1">Track every income, expense, and account transfer</p>
        </div>
        <Button onClick={() => handleOpenModal()} icon={Plus}>
          Add Transaction
        </Button>
      </div>

      {successMsg && (
        <div className="flex items-center gap-2 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
          <CheckCircle className="w-4 h-4" />
          <span>{successMsg}</span>
        </div>
      )}

      {error && <ErrorAlert message={error} />}

      {/* Filter Toolbar */}
      <Card className="flex flex-col md:flex-row gap-3 p-4">
        <div className="flex-1">
          <Input
            icon={Search}
            placeholder="Search by merchant or notes..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <div className="w-full md:w-48">
          <Select
            icon={Filter}
            value={selectedType}
            onChange={(e) => setSelectedType(e.target.value)}
            options={[{ value: '', label: 'All Types' }, ...TRANSACTION_TYPES]}
            placeholder="All Types"
          />
        </div>
        <div className="w-full md:w-56">
          <Select
            icon={Filter}
            value={selectedAccountId}
            onChange={(e) => setSelectedAccountId(e.target.value)}
            options={[{ value: '', label: 'All Accounts' }, ...accounts.map((a) => ({ value: String(a.accountId), label: a.accountName }))]}
            placeholder="All Accounts"
          />
        </div>
      </Card>

      {/* Transactions List */}
      {isLoading && transactions.length === 0 ? (
        <LoadingSpinner label="Loading transactions log..." />
      ) : filteredTransactions.length === 0 ? (
        <EmptyState
          icon={ArrowLeftRight}
          title="No transactions found"
          description="Try broadening your search filters or record a new transaction."
          actionLabel="Add Transaction"
          onAction={() => handleOpenModal()}
        />
      ) : (
        <div className="bg-white rounded-2xl overflow-hidden border border-slate-200 shadow-xs">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm border-collapse">
              <thead>
                <tr className="border-b border-slate-200 bg-slate-50 text-slate-700 text-[11px] font-bold uppercase tracking-wider">
                  <th className="py-3.5 px-4">Date</th>
                  <th className="py-3.5 px-4">Merchant / Notes</th>
                  <th className="py-3.5 px-4">Category</th>
                  <th className="py-3.5 px-4">Account</th>
                  <th className="py-3.5 px-4 text-right">Amount</th>
                  <th className="py-3.5 px-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filteredTransactions.map((tx) => {
                  const isIncome = tx.transactionType === 'INCOME';
                  const isTransfer = tx.transactionType === 'TRANSFER';
                  return (
                    <tr key={tx.transactionId} className="hover:bg-slate-50/80 transition-colors">
                      <td className="py-3.5 px-4 whitespace-nowrap text-slate-600 text-xs font-medium">
                        {formatDate(tx.transactionDate)}
                      </td>
                      <td className="py-3.5 px-4">
                        <div className="flex items-center gap-2.5">
                          <div
                            className={`p-2 rounded-xl ${
                              isIncome
                                ? 'bg-emerald-50 text-emerald-600 border border-emerald-100'
                                : isTransfer
                                ? 'bg-blue-50 text-blue-600 border border-blue-100'
                                : 'bg-rose-50 text-rose-600 border border-rose-100'
                            }`}
                          >
                            {isIncome ? (
                              <ArrowDownRight className="w-4 h-4" />
                            ) : isTransfer ? (
                              <ArrowLeftRight className="w-4 h-4" />
                            ) : (
                              <ArrowUpRight className="w-4 h-4" />
                            )}
                          </div>
                          <div>
                            <div className="font-bold text-slate-900">{tx.merchant || 'General Transaction'}</div>
                            {tx.notes && <div className="text-xs text-slate-500 mt-0.5">{tx.notes}</div>}
                          </div>
                        </div>
                      </td>
                      <td className="py-3.5 px-4 whitespace-nowrap">
                        <Badge variant="neutral" size="sm">
                          {categoryMap[tx.categoryId] || 'Uncategorized'}
                        </Badge>
                      </td>
                      <td className="py-3.5 px-4 whitespace-nowrap text-xs text-slate-600 font-medium">
                        {accountMap[tx.accountId] || `Account #${tx.accountId}`}
                      </td>
                      <td
                        className={`py-3.5 px-4 text-right whitespace-nowrap font-bold ${
                          isIncome ? 'text-emerald-600' : isTransfer ? 'text-blue-600' : 'text-slate-900'
                        }`}
                      >
                        {isIncome ? '+' : isTransfer ? '' : '-'}{formatCurrency(tx.amount, tx.currency)}
                      </td>
                      <td className="py-3.5 px-4 text-right whitespace-nowrap">
                        <div className="flex items-center justify-end gap-1">
                          <button
                            onClick={() => handleOpenModal(tx)}
                            className="p-1.5 rounded-lg text-slate-400 hover:text-slate-900 hover:bg-slate-100 transition-colors"
                          >
                            <Edit2 className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => handleDelete(tx.transactionId)}
                            className="p-1.5 rounded-lg text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition-colors"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Modal Form */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingId ? 'Edit Transaction' : 'Log New Transaction'}
        subtitle="Record your spending or incoming earnings"
      >
        <ErrorAlert message={formError} onClose={() => setFormError('')} />
        <form onSubmit={handleSubmit} className="space-y-4">
          <Select
            label="Type"
            value={formData.transactionType}
            onChange={(e) => setFormData({ ...formData, transactionType: e.target.value })}
            options={TRANSACTION_TYPES}
            required
          />

          <Input
            label="Amount (₹)"
            type="number"
            min="0.01"
            step="0.01"
            value={formData.amount}
            onChange={(e) => setFormData({ ...formData, amount: e.target.value })}
            placeholder="0.00"
            required
          />

          <Input
            label="Merchant / Payee"
            value={formData.merchant}
            onChange={(e) => setFormData({ ...formData, merchant: e.target.value })}
            placeholder="e.g. Swiggy, Amazon, Salary Credit"
            required
          />

          <div className="grid grid-cols-2 gap-3">
            <Select
              label="Account"
              value={formData.accountId}
              onChange={(e) => setFormData({ ...formData, accountId: e.target.value })}
              options={accounts.map((a) => ({ value: String(a.accountId), label: a.accountName }))}
              required
            />

            <Select
              label="Category"
              value={formData.categoryId}
              onChange={(e) => setFormData({ ...formData, categoryId: e.target.value })}
              options={categories.map((c) => ({ value: String(c.categoryId), label: c.categoryName }))}
            />
          </div>

          <Input
            label="Date"
            type="date"
            value={formData.transactionDate}
            onChange={(e) => setFormData({ ...formData, transactionDate: e.target.value })}
            required
          />

          <Input
            label="Notes (Optional)"
            value={formData.notes}
            onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
            placeholder="Add contextual details..."
          />

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <Button type="button" variant="secondary" onClick={() => setIsModalOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary" isLoading={isLoading}>
              {editingId ? 'Save Changes' : 'Log Transaction'}
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
