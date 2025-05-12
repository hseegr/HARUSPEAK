import { useState } from 'react';

import { useEditDiary } from '@/hooks/useDiaryQuery';

type AlertFunction = (
  title: string,
  message: string,
  isError?: boolean,
) => void;

export const useEditHandler = (
  summaryId: string,
  initialTitle: string,
  initialContent: string,
  showAlert: AlertFunction,
) => {
  const [isEditing, setIsEditing] = useState(false);
  const [editTitle, setEditTitle] = useState(initialTitle);
  const [editContent, setEditContent] = useState(initialContent);
  const [isSaving, setIsSaving] = useState(false);

  const editDiaryMutation = useEditDiary();

  // 수정 모드 시작
  const handleEditStart = () => {
    setEditTitle(initialTitle);
    setEditContent(initialContent);
    setIsEditing(true);
  };

  // 수정 취소
  const handleEditCancel = () => {
    setIsEditing(false);
    setEditTitle('');
    setEditContent('');
  };

  // 수정 저장
  const handleEditSave = () => {
    if (!summaryId) return;

    setIsSaving(true);

    editDiaryMutation({
      summaryId,
      title: editTitle,
      content: editContent,
    });

    // 저장 완료 후 상태 업데이트
    setTimeout(() => {
      setIsEditing(false);
      setIsSaving(false);
      showAlert('알림', '일기가 성공적으로 수정되었습니다.');
    }, 1000);
  };

  return {
    isEditing,
    editTitle,
    editContent,
    isSaving,
    setEditTitle,
    setEditContent,
    handleEditStart,
    handleEditCancel,
    handleEditSave,
  };
};
