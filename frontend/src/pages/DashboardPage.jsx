import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  Wallet,
  ArrowDownRight,
  ArrowUpRight,
  TrendingUp,
  Plus,
  ArrowRight,
  PieChart as PieIcon,
  CreditCard,
  RefreshCw,
  AlertTriangle,
} from 'lucide-react';
import {
  ResponsiveContainer,
  BarChart,
  Bar,
  Cell,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  ReferenceLine,
} from 'recharts';
import { Chart as ChartJS, ArcElement, Tooltip as ChartTooltip, Legend } from 'chart.js';
import { Doughnut } from 'react-chartjs-2';

import { useAccountStore } from '../store/useAccountStore';
import { useTransactionStore } from '../store/useTransactionStore';
import { useCategoryStore } from '../store/useCategoryStore';
import { Card } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Badge } from '../components/common/Badge';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { ErrorAlert } from '../components/common/ErrorAlert';
import { formatCurrency, formatDate } from '../utils/formatters';

ChartJS.register(ArcElement, ChartTooltip, Legend);

export const DashboardPage = () => {
  const { accounts, isLoading: accountsLoading, error: accountsError, fetchAccounts } = useAccountStore();
  const { transactions, isLoading: txLoading, error: txError, fetchTransactions } = useTransactionStore();
  const { categories, error: catError, fetchCategories } = useCategoryStore();

  const handleRetry = () => {
    fetchAccounts();
    fetchTransactions();
    fetchCategories();
  };

  useEffect(() => {
    fetchAccounts();
    fetchTransactions();
    fetchCategories();
  }, []);

  const isLoading = accountsLoading || txLoading;
  const dashboardError = accountsError || txError || catError;

  // Client-side computations
  const totalNetWorth = accounts.reduce((acc, a) => acc + (parseFloat(a.currentBalance) || 0), 0);

  const totalIncome = transactions
    .filter((t) => t.transactionType === 'INCOME')
    .reduce((acc, t) => acc + (parseFloat(t.amount) || 0), 0);

  const totalExpense = transactions
    .filter((t) => t.transactionType === 'EXPENSE')
    .reduce((acc, t) => acc + (parseFloat(t.amount) || 0), 0);

  const netSavings = totalIncome - totalExpense;
  const savingsRate = totalIncome > 0 ? Math.round((netSavings / totalIncome) * 100) : 0;

  // Recharts Monthly Income vs Expense Data
  const chartData = [
    { name: 'Income', amount: totalIncome, fill: '#10b981' },
    { name: 'Expenses', amount: totalExpense, fill: '#ef4444' },
    { name: 'Net Savings', amount: netSavings, fill: netSavings >= 0 ? '#10b981' : '#ef4444' },
  ];

  // Category Spending Donuts Data (Chart.js)
  const categoryMap = categories.reduce((acc, c) => ({ ...acc, [c.categoryId]: c.categoryName }), {});
  const categoryExpenseMap = {};
  transactions
    .filter((t) => t.transactionType === 'EXPENSE' && t.categoryId)
    .forEach((t) => {
      const catName = categoryMap[t.categoryId] || 'Other';
      categoryExpenseMap[catName] = (categoryExpenseMap[catName] || 0) + parseFloat(t.amount);
    });

  const categoryLabels = Object.keys(categoryExpenseMap);
  const categoryValues = Object.values(categoryExpenseMap);

  const doughnutData = {
    labels: categoryLabels.length > 0 ? categoryLabels : ['No Expenses Yet'],
    datasets: [
      {
        data: categoryValues.length > 0 ? categoryValues : [1],
        backgroundColor: [
          '#10b981',
          '#3b82f6',
          '#f59e0b',
          '#8b5cf6',
          '#ec4899',
          '#06b6d4',
          '#64748b',
        ],
        borderWidth: 0,
      },
    ],
  };

  const doughnutOptions = {
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          color: '#64748b',
          font: { size: 11 },
          padding: 12,
        },
      },
    },
    cutout: '70%',
    responsive: true,
    maintainAspectRatio: false,
  };

  // 1. Loading State
  if (isLoading && accounts.length === 0 && transactions.length === 0 && !dashboardError) {
    return <LoadingSpinner label="Initializing financial cockpit..." />;
  }

  // 2. API Error State (BUG-004)
  if (dashboardError && accounts.length === 0 && transactions.length === 0) {
    return (
      <div className="space-y-6">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Financial Cockpit</h1>
          <p className="text-xs text-slate-500 mt-1">Real-time overview of net assets, income, and cash flow</p>
        </div>
        <Card className="p-8 text-center border-rose-200 bg-rose-50/50">
          <div className="flex flex-col items-center justify-center space-y-4">
            <div className="p-4 rounded-2xl bg-rose-100 text-rose-600 border border-rose-200">
              <AlertTriangle className="w-8 h-8" />
            </div>
            <div>
              <h3 className="text-lg font-bold text-slate-900">Failed to Load Dashboard Data</h3>
              <p className="text-xs text-slate-500 mt-1 max-w-md mx-auto">{dashboardError}</p>
            </div>
            <Button onClick={handleRetry} icon={RefreshCw} className="mt-2">
              Retry Loading Dashboard
            </Button>
          </div>
        </Card>
      </div>
    );
  }

  // 3. Genuine Empty State for Brand-New User (BUG-007)
  if (!isLoading && !dashboardError && accounts.length === 0 && transactions.length === 0) {
    return (
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-slate-900">Financial Cockpit</h1>
            <p className="text-xs text-slate-500 mt-1">Real-time overview of net assets, income, and cash flow</p>
          </div>
        </div>

        <Card className="p-10 text-center border-slate-200 shadow-sm">
          <div className="flex flex-col items-center justify-center space-y-4">
            <div className="p-4 rounded-2xl bg-emerald-50 text-emerald-600 border border-emerald-100">
              <Wallet className="w-10 h-10" />
            </div>
            <div>
              <h3 className="text-xl font-extrabold text-slate-900">Welcome to FinPilot!</h3>
              <p className="text-sm text-slate-500 mt-1 max-w-md mx-auto">
                Your financial cockpit is ready. Connect a bank/wallet account or log your first transaction to start monitoring your finances.
              </p>
            </div>
            <div className="flex flex-wrap items-center justify-center gap-3 pt-2">
              <Link to="/accounts">
                <Button icon={Plus}>Add Account</Button>
              </Link>
              <Link to="/transactions">
                <Button variant="secondary" icon={Plus}>Add Transaction</Button>
              </Link>
            </div>
          </div>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Financial Cockpit</h1>
          <p className="text-xs text-slate-500 mt-1">Real-time overview of net assets, income, and cash flow</p>
        </div>
        <div className="flex items-center gap-3">
          <Link to="/transactions">
            <Button icon={Plus}>Log Transaction</Button>
          </Link>
        </div>
      </div>

      {/* Overview Metric Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card className="border-slate-200/90 shadow-xs hover:border-slate-300">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-500 font-semibold uppercase tracking-wider">Total Net Worth</span>
            <div className="p-2.5 rounded-xl bg-blue-50 text-blue-600 border border-blue-100 shadow-xs">
              <Wallet className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-3">
            <h3 className="text-2xl font-extrabold text-slate-900">{formatCurrency(totalNetWorth)}</h3>
            <span className="text-xs text-slate-500 mt-1 block font-medium">{accounts.length} Accounts Connected</span>
          </div>
        </Card>

        <Card className="border-slate-200/90 shadow-xs hover:border-slate-300">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-500 font-semibold uppercase tracking-wider">Total Income</span>
            <div className="p-2.5 rounded-xl bg-emerald-50 text-emerald-600 border border-emerald-100 shadow-xs">
              <ArrowDownRight className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-3">
            <h3 className="text-2xl font-extrabold text-emerald-600">{formatCurrency(totalIncome)}</h3>
            <span className="text-xs text-emerald-700 mt-1 block font-medium">Total Inflow</span>
          </div>
        </Card>

        <Card className="border-slate-200/90 shadow-xs hover:border-slate-300">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-500 font-semibold uppercase tracking-wider">Total Expenses</span>
            <div className="p-2.5 rounded-xl bg-rose-50 text-rose-600 border border-rose-100 shadow-xs">
              <ArrowUpRight className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-3">
            <h3 className="text-2xl font-extrabold text-rose-600">{formatCurrency(totalExpense)}</h3>
            <span className="text-xs text-rose-700 mt-1 block font-medium">Total Outflow</span>
          </div>
        </Card>

        <Card className="border-slate-200/90 shadow-xs hover:border-slate-300">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-500 font-semibold uppercase tracking-wider">Savings Rate</span>
            <div className="p-2.5 rounded-xl bg-purple-50 text-purple-600 border border-purple-100 shadow-xs">
              <TrendingUp className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-3">
            <h3 className="text-2xl font-extrabold text-purple-600">{savingsRate}%</h3>
            <span className="text-xs text-slate-500 mt-1 block font-medium">{formatCurrency(netSavings)} Net Saved</span>
          </div>
        </Card>
      </div>

      {/* Visual Analytics Section */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Recharts Bar Visualization */}
        <Card title="Cash Flow Overview" subtitle="Income vs Expense Distribution" className="lg:col-span-2">
          <div className="h-64 w-full pt-4">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={chartData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" opacity={0.8} />
                <XAxis dataKey="name" stroke="#64748b" fontSize={12} tickLine={false} />
                <YAxis stroke="#64748b" fontSize={12} tickLine={false} />
                <Tooltip
                  contentStyle={{ backgroundColor: '#ffffff', borderColor: '#e2e8f0', borderRadius: '12px', color: '#0f172a', boxShadow: '0 10px 25px -5px rgba(0, 0, 0, 0.08)' }}
                  formatter={(val) => [formatCurrency(val), 'Amount']}
                />
                <ReferenceLine y={0} stroke="#cbd5e1" strokeDasharray="3 3" />
                <Bar dataKey="amount" radius={[4, 4, 0, 0]}>
                  {chartData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.fill} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
        </Card>

        {/* Chart.js Category Donuts */}
        <Card title="Category Spending" subtitle="Breakdown by Category">
          <div className="h-64 relative flex items-center justify-center pt-2">
            <Doughnut data={doughnutData} options={doughnutOptions} />
          </div>
        </Card>
      </div>

      {/* Recent Transactions List */}
      <Card
        title="Recent Transactions"
        subtitle="Latest financial activity across all accounts"
        action={
          <Link to="/transactions" className="text-xs font-semibold text-emerald-600 hover:text-emerald-700 hover:underline inline-flex items-center gap-1">
            View All <ArrowRight className="w-3.5 h-3.5" />
          </Link>
        }
      >
        {transactions.length === 0 ? (
          <p className="text-xs text-slate-500 py-6 text-center">No transactions logged yet.</p>
        ) : (
          <div className="divide-y divide-slate-100">
            {transactions.slice(0, 5).map((tx) => {
              const isIncome = tx.transactionType === 'INCOME';
              return (
                <div key={tx.transactionId} className="py-3 flex items-center justify-between hover:bg-slate-50 px-2 rounded-xl transition-colors">
                  <div className="flex items-center gap-3">
                    <div
                      className={`p-2.5 rounded-xl ${
                        isIncome ? 'bg-emerald-50 text-emerald-600 border border-emerald-100' : 'bg-rose-50 text-rose-600 border border-rose-100'
                      }`}
                    >
                      {isIncome ? <ArrowDownRight className="w-4 h-4" /> : <ArrowUpRight className="w-4 h-4" />}
                    </div>
                    <div>
                      <h4 className="text-sm font-bold text-slate-900">{tx.merchant || 'General Transaction'}</h4>
                      <p className="text-xs text-slate-500">{formatDate(tx.transactionDate)}</p>
                    </div>
                  </div>
                  <span className={`text-sm font-extrabold ${isIncome ? 'text-emerald-600' : 'text-slate-900'}`}>
                    {isIncome ? '+' : '-'}{formatCurrency(tx.amount, tx.currency)}
                  </span>
                </div>
              );
            })}
          </div>
        )}
      </Card>
    </div>
  );
};
