import { ChangeEvent, useCallback, useState } from 'react';

import { useQueryClient } from '@tanstack/react-query';

import { updateMoment } from '@/apis/todayApi';
import {
  convert24To12HourFormat,
  get24HourFormat,
  parseMomentTime,
  updateMomentTime,
} from '@/lib/timeUtils';
import { MomentContent } from '@/types/common';
import { UpdateMomentRequest } from '@/types/today';

export const useMomentEdit = (
  initialMoment: MomentContent,
  onClose: () => void,
) => {
  const queryClient = useQueryClient();
  const [editedMoment, setEditedMoment] =
    useState<MomentContent>(initialMoment);
  const [newTag, setNewTag] = useState('');
  const [isSaving, setIsSaving] = useState(false);
  const [deletedImages, setDeletedImages] = useState<string[]>([]);
  const createdAt = initialMoment.createdAt;

  // 순간 기록의 날짜와 시간 파싱
  const { date } = parseMomentTime(editedMoment.momentTime);
  const currentTime = get24HourFormat(
    parseMomentTime(editedMoment.momentTime).time,
  );

  // 저장 버튼 비활성화 조건: 내용과 이미지가 모두 비어있는 경우
  const isSaveDisabled =
    editedMoment.content.trim() === '' && editedMoment.images.length === 0;

  // 시간 변경 핸들러 : 24시간 형식의 입력을 12시간 형식으로 변환하여 저장
  const handleTimeChange = (e: ChangeEvent<HTMLInputElement>) => {
    const inputTime = e.target.value;
    const formattedTime = convert24To12HourFormat(inputTime);
    setEditedMoment(prev => {
      const updatedMomentTime = updateMomentTime(
        prev.momentTime,
        formattedTime,
      );
      return {
        ...prev,
        momentTime: updatedMomentTime,
      };
    });
  };

  // 내용 변경 핸들러
  const handleContentChange = (value: string) => {
    setEditedMoment(prev => ({ ...prev, content: value }));
  };

  // 이미지 삭제 핸들러 : 이미지를 목록에서 제거하고 삭제된 이미지 목록에 추가
  const handleDeleteImage = (index: number) => {
    const deletedImage = editedMoment.images[index];
    setEditedMoment(prev => ({
      ...prev,
      images: prev.images.filter((_, i) => i !== index),
    }));
    setDeletedImages(prev => [...prev, deletedImage]);
  };

  // 태그 삭제 핸들러 : 선택한 인덱스의 태그를 목록에서 제거
  const handleDeleteTag = (index: number) => {
    setEditedMoment(prev => ({
      ...prev,
      tags: prev.tags.filter((_, i) => i !== index),
    }));
  };

  // 태그 추가 핸들러 : 입력된 태그가 비어있지 않은 경우에만 추가
  const handleAddTag = () => {
    if (newTag.trim() !== '') {
      setEditedMoment(prev => ({
        ...prev,
        tags: [...prev.tags, newTag.trim()],
      }));
      setNewTag('');
    }
  };

  // 저장 핸들러 : 서버 전송 & 성공 시 캐시 무효화 & 실패 시 에러 로깅
  const handleSave = async () => {
    try {
      setIsSaving(true);

      const updateData: UpdateMomentRequest = {
        momentTime: editedMoment.momentTime,
        content: editedMoment.content,
        images: editedMoment.images,
        tags: editedMoment.tags,
        deletedImages,
      };

      if (!createdAt) {
        throw new Error('createdAt이 없습니다.');
      }

      await updateMoment(createdAt, updateData);
      await queryClient.invalidateQueries({ queryKey: ['todayDiary'] });
      onClose();
    } catch (error) {
      console.error('저장 실패:', error);
    } finally {
      setIsSaving(false);
    }
  };

  // 상태 초기화 함수 : 다이얼로그 열리거나 수정 취소 시 돌려놓기용
  const resetState = useCallback(() => {
    setEditedMoment(initialMoment);
    setNewTag('');
    setDeletedImages([]);
  }, [initialMoment]);

  return {
    editedMoment,
    newTag,
    setNewTag,
    isSaving,
    deletedImages,
    date,
    currentTime,
    isSaveDisabled,
    handleTimeChange,
    handleContentChange,
    handleDeleteImage,
    handleDeleteTag,
    handleAddTag,
    handleSave,
    resetState,
  };
};
