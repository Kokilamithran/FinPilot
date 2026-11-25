import React, { useEffect, useState } from 'react';
import { Bell, Check, Trash2, Info, CheckCircle } from 'lucide-react';
import { notificationApi } from '../api/notificationApi';
import { Card } from '../components/common/Card';
import { Badge } from '../components/common/Badge';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { EmptyState } from '../components/common/EmptyState';
import { ErrorAlert } from '../components/common/ErrorAlert';
import { formatDateTime } from '../utils/formatters';

export const NotificationsPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  const loadData = async () => {
    setIsLoading(true);
    setError('');
    try {
      const data = await notificationApi.getAll();
      setNotifications(data);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to load notifications');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleMarkAsRead = async (item) => {
    setSuccessMsg('');
    try {
      await notificationApi.update(item.notificationId, {
        ...item,
        status: 'READ',
      });
      setSuccessMsg('Notification marked as read');
      loadData();
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to update notification');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this notification?')) {
      return;
    }
    setError('');
    setSuccessMsg('');
    try {
      await notificationApi.delete(id);
      setSuccessMsg('Notification deleted');
      loadData();
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to delete notification');
    }
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Notifications & Alerts</h1>
        <p className="text-xs text-slate-500 mt-1">System updates, budget warnings, and recurring bill alerts</p>
      </div>

      {successMsg && (
        <div className="flex items-center gap-2 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
          <CheckCircle className="w-4 h-4" />
          <span>{successMsg}</span>
        </div>
      )}

      {error && <ErrorAlert message={error} />}

      {isLoading && notifications.length === 0 ? (
        <LoadingSpinner label="Loading notifications..." />
      ) : notifications.length === 0 ? (
        <EmptyState
          icon={Bell}
          title="No unread notifications"
          description="You are completely caught up with your financial alerts."
        />
      ) : (
        <div className="space-y-3">
          {notifications.map((item) => (
            <Card key={item.notificationId} className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 py-4">
              <div className="flex items-center gap-3">
                <div className="p-2.5 rounded-xl bg-blue-50 text-blue-600 border border-blue-100 shadow-xs shrink-0">
                  <Info className="w-5 h-5" />
                </div>
                <div>
                  <h4 className="text-sm font-bold text-slate-900">{item.message}</h4>
                  <span className="text-xs text-slate-500 mt-0.5 block">{formatDateTime(item.createdDate)}</span>
                </div>
              </div>
              <div className="flex items-center gap-2">
                <Badge variant={item.status === 'READ' ? 'neutral' : 'indigo'} size="sm">
                  {item.status || 'UNREAD'}
                </Badge>
                {item.status !== 'READ' && (
                  <button
                    onClick={() => handleMarkAsRead(item)}
                    title="Mark as Read"
                    className="p-1.5 rounded-lg text-slate-400 hover:text-emerald-600 hover:bg-emerald-50 transition-colors"
                  >
                    <Check className="w-4 h-4" />
                  </button>
                )}
                <button
                  onClick={() => handleDelete(item.notificationId)}
                  title="Delete"
                  className="p-1.5 rounded-lg text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition-colors"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};
