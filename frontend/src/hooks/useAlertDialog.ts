import { useState } from 'react';

export const useAlertDialog = () => {
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertInfo, setAlertInfo] = useState({
    title: '',
    message: '',
    confirmText: '확인',
    confirmColor: 'bg-haru-green',
  });

  const showAlert = (title: string, message: string, isError = false) => {
    setAlertInfo({
      title,
      message,
      confirmText: '확인',
      confirmColor: isError ? 'bg-red-500' : 'bg-haru-green',
    });
    setAlertOpen(true);
  };

  return {
    alertOpen,
    setAlertOpen,
    alertInfo,
    showAlert,
  };
};
