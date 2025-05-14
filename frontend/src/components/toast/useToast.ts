import { ToastOptions, toast } from 'react-toastify';

type ToastType = 'info' | 'success' | 'warning' | 'error';

const defaultOptions: ToastOptions = {
  autoClose: 3000,
  position: 'top-center',
};

export const useToast = () => {
  const showToast = (message: string, type: ToastType = 'info') => {
    toast[type](message, defaultOptions);
  };

  return { showToast };
};
