import { useState } from 'react';

import { toast } from 'react-toastify';

import { useEditDiary } from '@/hooks/useDiaryQuery';

export const useEditHandler = (
  summaryId: string,
  initialTitle: string,
  initialContent: string,
) => {
  const [isEditing, setIsEditing] = useState(false);
  const [editTitle, setEditTitle] = useState(initialTitle);
  const [editContent, setEditContent] = useState(initialContent);

  const { mutate: editDiaryMutation, isPending: isSaving } = useEditDiary();

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

    if (!editTitle.trim() || !editContent.trim()) {
      toast.warning('제목과 내용을 모두 입력해주세요.');
      return;
    }

    editDiaryMutation({
      summaryId,
      title: editTitle,
      content: editContent,
    });

    // 모든 처리는 useMutation의 콜백에서 이루어지므로
    // 여기서는 편집 모드만 종료
    setIsEditing(false);
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
