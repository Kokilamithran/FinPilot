import React, { useEffect, useState } from 'react';
import { Plus, Tags, Trash2, Edit2, ShieldAlert, CheckCircle } from 'lucide-react';
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
import { CATEGORY_TYPES } from '../utils/constants';

export const CategoriesPage = () => {
  const { categories, isLoading, error, fetchCategories, createCategory, updateCategory, deleteCategory } =
    useCategoryStore();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');
  const [formData, setFormData] = useState({
    categoryName: '',
    categoryType: 'EXPENSE',
    icon: 'tag',
    colour: '#6366f1',
    isSystem: false,
    displayOrder: 0,
  });
  const [formError, setFormError] = useState('');

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleOpenModal = (category = null) => {
    setFormError('');
    if (category) {
      setEditingId(category.categoryId);
      setFormData({
        categoryName: category.categoryName || '',
        categoryType: category.categoryType || 'EXPENSE',
        icon: category.icon || 'tag',
        colour: category.colour || '#6366f1',
        isSystem: category.isSystem || false,
        displayOrder: category.displayOrder || 0,
      });
    } else {
      setEditingId(null);
      setFormData({
        categoryName: '',
        categoryType: 'EXPENSE',
        icon: 'tag',
        colour: '#6366f1',
        isSystem: false,
        displayOrder: 0,
      });
    }
    setIsModalOpen(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setFormError('');
    setSuccessMsg('');
    try {
      if (editingId) {
        await updateCategory(editingId, formData);
        setSuccessMsg('Category updated successfully!');
      } else {
        await createCategory(formData);
        setSuccessMsg('Category created successfully!');
      }
      setIsModalOpen(false);
      setTimeout(() => setSuccessMsg(''), 4000);
    } catch (err) {
      setFormError(err.response?.data?.message || err.message || 'Failed to save category');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this category?')) {
      try {
        await deleteCategory(id);
        setSuccessMsg('Category deleted successfully!');
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
          <h1 className="text-2xl font-bold text-slate-900">Category Manager</h1>
          <p className="text-xs text-slate-500 mt-1">Organize transactions into system and custom categories</p>
        </div>
        <Button onClick={() => handleOpenModal()} icon={Plus}>
          Add Custom Category
        </Button>
      </div>

      {successMsg && (
        <div className="flex items-center gap-2 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
          <CheckCircle className="w-4 h-4" />
          <span>{successMsg}</span>
        </div>
      )}

      {error && <ErrorAlert message={error} />}

      {isLoading && categories.length === 0 ? (
        <LoadingSpinner label="Loading categories..." />
      ) : categories.length === 0 ? (
        <EmptyState
          icon={Tags}
          title="No categories found"
          description="Create your first category to group your income and expenses."
          actionLabel="Add Category"
          onAction={() => handleOpenModal()}
        />
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
          {categories.map((category) => (
            <Card key={category.categoryId} hoverable className="relative group">
              <div className="flex items-start justify-between">
                <div className="flex items-center gap-3">
                  <div
                    className="w-10 h-10 rounded-xl flex items-center justify-center text-white font-bold text-sm shadow-xs"
                    style={{ backgroundColor: category.colour || '#10b981' }}
                  >
                    {category.categoryName ? category.categoryName.charAt(0).toUpperCase() : <Tags className="w-5 h-5" />}
                  </div>
                  <div>
                    <h3 className="text-sm font-bold text-slate-900">{category.categoryName}</h3>
                    <div className="flex items-center gap-1.5 mt-1">
                      <Badge
                        variant={category.categoryType === 'INCOME' ? 'emerald' : 'rose'}
                        size="sm"
                      >
                        {category.categoryType}
                      </Badge>
                      {category.isSystem && (
                        <Badge variant="sky" size="sm">System</Badge>
                      )}
                    </div>
                  </div>
                </div>

                {!category.isSystem && (
                  <div className="flex items-center gap-1 opacity-80 group-hover:opacity-100 transition-opacity">
                    <button
                      onClick={() => handleOpenModal(category)}
                      className="p-1.5 rounded-lg text-slate-400 hover:text-slate-900 hover:bg-slate-100 transition-colors"
                    >
                      <Edit2 className="w-4 h-4" />
                    </button>
                    <button
                      onClick={() => handleDelete(category.categoryId)}
                      className="p-1.5 rounded-lg text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition-colors"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                )}
              </div>
            </Card>
          ))}
        </div>
      )}

      {/* Modal Form */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingId ? 'Edit Category' : 'Create Category'}
        subtitle="Custom categories allow tailored financial tracking"
      >
        <ErrorAlert message={formError} onClose={() => setFormError('')} />
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Category Name"
            value={formData.categoryName}
            onChange={(e) => setFormData({ ...formData, categoryName: e.target.value })}
            placeholder="e.g. Groceries, Freelance Income"
            required
          />

          <Select
            label="Category Type"
            value={formData.categoryType}
            onChange={(e) => setFormData({ ...formData, categoryType: e.target.value })}
            options={CATEGORY_TYPES}
            required
          />

          <Input
            label="Theme Color"
            type="color"
            value={formData.colour}
            onChange={(e) => setFormData({ ...formData, colour: e.target.value })}
            className="h-10 cursor-pointer"
          />

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <Button type="button" variant="secondary" onClick={() => setIsModalOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary" isLoading={isLoading}>
              {editingId ? 'Save Changes' : 'Create Category'}
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
