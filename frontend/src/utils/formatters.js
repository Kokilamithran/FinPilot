export const formatCurrency = (amount, currency = 'INR') => {
  if (amount === undefined || amount === null) return '₹0.00';
  
  const numericAmount = typeof amount === 'string' ? parseFloat(amount) : amount;
  
  try {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: currency || 'INR',
      maximumFractionDigits: 2,
    }).format(numericAmount);
  } catch (e) {
    return `₹${numericAmount.toFixed(2)}`;
  }
};

export const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  try {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    }).format(date);
  } catch (e) {
    return dateString;
  }
};

export const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return 'N/A';
  try {
    const date = new Date(dateTimeString);
    return new Intl.DateTimeFormat('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date);
  } catch (e) {
    return dateTimeString;
  }
};
