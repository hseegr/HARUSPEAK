// Library 페이지에서 일기 선택 및 삭제 관련 로직을 관리하는 훅
import { useCallback, useState } from 'react';

import { toast } from 'react-toastify';

import { useDeleteDiary } from '@/hooks/useLibraryQuery';

export const useLibraryDelete = () => {
  const [isSelectionMode, setIsSelectionMode] = useState(false);
  const [selectedIds, setSelectedIds] = useState<number[]>([]);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);

  const deleteDiary = useDeleteDiary();

  const handleToggleSelection = useCallback(() => {
    setIsSelectionMode(prev => !prev);
    setSelectedIds([]);
  }, []);

  const handleSelect = useCallback((summaryId: number) => {
    setSelectedIds(prev =>
      prev.includes(summaryId)
        ? prev.filter(id => id !== summaryId)
        : [...prev, summaryId],
    );
  }, []);

  const handleReset = useCallback(() => {
    setSelectedIds([]);
  }, []);

  // 삭제 버튼 클릭 시 확인
  const handleDeleteClick = useCallback(() => {
    if (selectedIds.length === 0) {
      toast.warning('선택된 일기가 없습니다.');
      return;
    }
    setIsDeleteDialogOpen(true);
  }, [selectedIds]);

  // 실제 삭제 처리
  const handleConfirmDelete = useCallback(() => {
    if (selectedIds.length === 0) return;

    setIsDeleting(true);

    // 단순히 mutation 함수 호출 - 성공/실패 처리는 useMutation에서 자동 처리
    Promise.all(selectedIds.map(id => deleteDiary.mutateAsync(id))).finally(
      () => {
        // 성공/실패와 무관하게 상태 정리
        setIsDeleting(false);
        setIsSelectionMode(false);
        setSelectedIds([]);
        setIsDeleteDialogOpen(false);
      },
    );
  }, [deleteDiary, selectedIds]);

  return {
    selectionState: {
      isSelectionMode,
      selectedIds,
      isDeleteDialogOpen,
      isDeleting,
    },
    selectionActions: {
      handleToggleSelection,
      handleSelect,
      handleReset,
      handleDeleteClick,
      handleConfirmDelete,
      setIsDeleteDialogOpen,
    },
  };
};
