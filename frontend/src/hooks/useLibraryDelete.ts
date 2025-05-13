// Library 페이지에서 일기 선택 및 삭제 관련 로직을 관리하는 훅
import { useCallback, useState } from 'react';

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

  // 삭제 버튼 클릭 시 Dialog 열기
  const handleDeleteClick = useCallback(() => {
    if (selectedIds.length > 0) {
      setIsDeleteDialogOpen(true);
    }
  }, [selectedIds]);

  // 실제 삭제 처리 (개별 요청)
  const handleConfirmDelete = useCallback(async () => {
    if (selectedIds.length === 0) return;

    try {
      setIsDeleting(true);
      console.log('삭제 시작:', selectedIds);

      // 각 일기를 순차적으로 개별 삭제 (Promise.all 대신 for문 사용)
      for (const id of selectedIds) {
        try {
          await deleteDiary(id);
          console.log(`일기 ${id} 삭제 성공`);
        } catch (error) {
          console.error(`일기 ${id} 삭제 실패:`, error);
        }
      }
      setIsSelectionMode(false);
      setSelectedIds([]);
      setIsDeleteDialogOpen(false);
    } catch (error) {
      console.error('일기 삭제 중 오류 발생:', error);
    } finally {
      setIsDeleting(false);
    }
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
