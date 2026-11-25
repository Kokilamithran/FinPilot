import React, { useEffect, useState } from 'react';
import { FileSpreadsheet, Trash2, CheckCircle2, AlertCircle } from 'lucide-react';
import { importApi } from '../api/importApi';
import { Card } from '../components/common/Card';
import { Badge } from '../components/common/Badge';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { EmptyState } from '../components/common/EmptyState';
import { ErrorAlert } from '../components/common/ErrorAlert';
import { formatDateTime } from '../utils/formatters';

export const ImportHistoryPage = () => {
  const [history, setHistory] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  const loadData = async () => {
    setIsLoading(true);
    setError('');
    try {
      const data = await importApi.getAll();
      setHistory(data);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to load import history');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this import history record?')) {
      return;
    }
    setError('');
    setSuccessMsg('');
    try {
      await importApi.delete(id);
      setSuccessMsg('Import history record deleted successfully');
      loadData();
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to delete import history record');
    }
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Import History</h1>
        <p className="text-xs text-slate-500 mt-1">Audit log of imported bank statements and CSV records</p>
      </div>

      {successMsg && (
        <div className="flex items-center gap-2 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
          <CheckCircle2 className="w-4 h-4" />
          <span>{successMsg}</span>
        </div>
      )}

      {error && <ErrorAlert message={error} onClose={() => setError('')} />}

      {isLoading && history.length === 0 ? (
        <LoadingSpinner label="Loading statement import records..." />
      ) : history.length === 0 ? (
        <EmptyState
          icon={FileSpreadsheet}
          title="No bank statements imported"
          description="Statement import history will appear here once files are uploaded."
        />
      ) : (
        <div className="bg-white rounded-2xl overflow-hidden border border-slate-200 shadow-xs">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm border-collapse">
              <thead>
                <tr className="border-b border-slate-200 bg-slate-50 text-slate-700 text-[11px] font-bold uppercase tracking-wider">
                  <th className="py-3.5 px-4">File Name</th>
                  <th className="py-3.5 px-4">Format</th>
                  <th className="py-3.5 px-4">Records</th>
                  <th className="py-3.5 px-4">Success</th>
                  <th className="py-3.5 px-4">Status</th>
                  <th className="py-3.5 px-4">Imported At</th>
                  <th className="py-3.5 px-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {history.map((item) => (
                  <tr key={item.importId} className="hover:bg-slate-50/80 transition-colors">
                    <td className="py-3.5 px-4 font-bold text-slate-900">{item.fileName}</td>
                    <td className="py-3.5 px-4 uppercase text-xs font-semibold text-slate-600">{item.fileFormat}</td>
                    <td className="py-3.5 px-4 text-slate-700 font-medium">{item.totalRecords}</td>
                    <td className="py-3.5 px-4 text-emerald-600 font-extrabold">{item.successCount}</td>
                    <td className="py-3.5 px-4">
                      <Badge variant={item.status === 'COMPLETED' || item.status === 'SUCCESS' ? 'emerald' : 'rose'} size="sm">
                        {item.status}
                      </Badge>
                    </td>
                    <td className="py-3.5 px-4 text-xs text-slate-500 font-medium">{formatDateTime(item.importedAt)}</td>
                    <td className="py-3.5 px-4 text-right">
                      <button
                        onClick={() => handleDelete(item.importId)}
                        className="p-1.5 rounded-lg text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition-colors"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};
