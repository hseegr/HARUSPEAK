// 필터 다이얼로그 상태를 관리하는 훅
import { useState } from 'react';

interface DialogState {
  isFilterOpen: boolean;
  isDateFilterOpen: boolean;
}

interface DialogActions {
  setIsFilterOpen: (isOpen: boolean) => void;
  setIsDateFilterOpen: (isOpen: boolean) => void;
  handleFilterClick: () => void;
  handleDateFilterClick: () => void;
}

export const useFilterDialogs = () => {
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [isDateFilterOpen, setIsDateFilterOpen] = useState(false);

  const handleFilterClick = () => {
    setIsFilterOpen(true);
  };

  const handleDateFilterClick = () => {
    setIsDateFilterOpen(true);
  };

  const dialogState: DialogState = {
    isFilterOpen,
    isDateFilterOpen,
  };

  const dialogActions: DialogActions = {
    setIsFilterOpen,
    setIsDateFilterOpen,
    handleFilterClick,
    handleDateFilterClick,
  };

  return { dialogState, dialogActions };
};
