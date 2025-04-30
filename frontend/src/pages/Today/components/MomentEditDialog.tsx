import { ChangeEvent, useEffect, useState } from 'react';

import { useQueryClient } from '@tanstack/react-query';

import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import {
  convert24To12HourFormat,
  get24HourFormat,
  parseMomentTime,
  updateMomentTime,
} from '@/lib/timeUtils';
import { updateMoment } from '@/mock/mockTodayApi';
import { MomentContent } from '@/types/common';
import { UpdateMomentRequest } from '@/types/today';

interface MomentEditDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  moment: MomentContent;
}

const MomentEditDialog = ({
  open,
  onOpenChange,
  moment,
}: MomentEditDialogProps) => {
  const queryClient = useQueryClient();
  const [content, setContent] = useState(moment.content);
  const [images, setImages] = useState(moment.images);
  const [tags, setTags] = useState(moment.tags);
  const [newTag, setNewTag] = useState('');
  const [isSaving, setIsSaving] = useState(false);
  const [deleteImages, setDeleteImages] = useState<string[]>([]);
  const [originalState, setOriginalState] = useState({
    content: moment.content,
    images: moment.images,
    tags: moment.tags,
  });

  // Dialog가 열릴 때마다 상태 초기화
  useEffect(() => {
    if (open) {
      setContent(moment.content);
      setImages(moment.images);
      setTags(moment.tags);
      setNewTag('');
      setOriginalState({
        content: moment.content,
        images: moment.images,
        tags: moment.tags,
      });
    }
  }, [open, moment]);

  // 취소 버튼 클릭 시 원래 상태로 복구
  const handleCancel = () => {
    setContent(originalState.content);
    setImages(originalState.images);
    setTags(originalState.tags);
    setNewTag('');
    onOpenChange(false);
  };

  // 저장 버튼 비활성화 조건
  const isSaveDisabled = content.trim() === '' && images.length === 0;

  // momentTime 파싱 및 포맷팅
  const { date, time } = parseMomentTime(moment.momentTime);
  const [editedTime, setEditedTime] = useState(time);

  // 시간 입력 핸들러
  const handleTimeChange = (e: ChangeEvent<HTMLInputElement>) => {
    const inputTime = e.target.value;
    const formattedTime = convert24To12HourFormat(inputTime);
    setEditedTime(formattedTime);
  };

  // 이미지 삭제
  const handleDeleteImage = (index: number) => {
    const deletedImage = images[index];
    setImages(prev => prev.filter((_, i) => i !== index));
    setDeleteImages(prev => [...prev, deletedImage]);
  };

  // 태그 삭제
  const handleDeleteTag = (index: number) => {
    setTags(prev => prev.filter((_, i) => i !== index));
  };

  // 태그 추가
  const handleAddTag = () => {
    if (newTag.trim() !== '') {
      setTags(prev => [...prev, newTag.trim()]);
      setNewTag('');
    }
  };

  // 저장
  const handleSave = async () => {
    try {
      setIsSaving(true);
      const updatedMomentTime = updateMomentTime(moment.momentTime, editedTime);

      const updateData: UpdateMomentRequest = {
        momentTime: updatedMomentTime,
        content,
        images,
        tags,
        deleteImages,
      };

      await updateMoment(updateData);

      // 저장 성공 후 리페칭
      await queryClient.invalidateQueries({ queryKey: ['todayDiary'] });
      onOpenChange(false);
    } catch (error) {
      console.error('저장 실패:', error);
      // TODO: 에러 처리 UI 추가
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent
        showCloseButton={false}
        layout='stacked'
        className='min-h-full max-w-96'
      >
        <DialogDescription className='sr-only'>
          오늘의 순간 기록을 수정하는 대화상자입니다. 시간, 내용, 이미지, 태그를
          수정할 수 있습니다.
        </DialogDescription>
        <DialogHeader className='flex flex-row items-center justify-between'>
          <DialogClose asChild>
            <button onClick={handleCancel}>취소</button>
          </DialogClose>
          <DialogTitle>오늘의 순간 수정</DialogTitle>
          <button
            onClick={handleSave}
            disabled={isSaving || isSaveDisabled}
            className={`rounded-full px-3 py-1.5 text-sm ${
              isSaving || isSaveDisabled
                ? 'cursor-not-allowed bg-gray-300 text-gray-500'
                : 'bg-[#41644A] text-white'
            }`}
          >
            {isSaving ? '저장 중...' : '저장'}
          </button>
        </DialogHeader>
        <div className='flex flex-col gap-2'>
          <div className='text-end text-sm text-gray-600'>{date}</div>
        </div>
        <div className='flex flex-col gap-4 rounded-xl bg-haru-beige p-3'>
          {/* 날짜 & 시간 */}
          <div>
            <input
              type='time'
              value={get24HourFormat(editedTime)}
              onChange={handleTimeChange}
              className='rounded-full border bg-haru-yellow p-2 text-sm focus:outline-[#41644A]'
            />
          </div>

          {/* 이미지 리스트 */}
          <div className='grid grid-cols-3 gap-2'>
            {Array.from({ length: images.length }).map((_, idx) => (
              <div
                key={idx}
                className='relative h-24 w-full rounded-lg bg-gray-100'
              >
                {images[idx] ? (
                  <>
                    <img
                      src={images[idx]}
                      alt={`image-${idx}`}
                      className='h-full w-full rounded-lg object-cover'
                    />
                    <button
                      onClick={() => handleDeleteImage(idx)}
                      className='absolute right-1 top-1 rounded-full bg-black bg-opacity-50 p-0.5 px-1 text-xs text-white'
                    >
                      ✕
                    </button>
                  </>
                ) : null}
              </div>
            ))}
          </div>

          {/* 순간 기록 */}
          <div className='flex flex-col gap-1'>
            <textarea
              value={content}
              onChange={e => setContent(e.target.value)}
              rows={4}
              className='w-full resize-none rounded-md border border-gray-300 p-2 text-sm focus:outline-[#41644A]'
              placeholder='순간의 기록을 입력하세요'
            />
          </div>

          {/* 태그 추가 */}
          <div className='flex flex-col gap-2'>
            <div className='flex gap-2'>
              <input
                value={newTag}
                onChange={e => setNewTag(e.target.value)}
                className='flex-1 rounded-md border border-gray-300 p-2 text-sm focus:outline-[#41644A]'
                placeholder='태그 입력'
              />
              <button
                onClick={handleAddTag}
                disabled={newTag.trim() === ''}
                className='rounded-full bg-[#41644A] px-3 py-1.5 text-sm text-white disabled:bg-gray-300'
              >
                태그 추가
              </button>
            </div>

            {/* 현재 태그 목록 */}
            <div className='flex flex-wrap gap-2'>
              {tags.map((tag, idx) => (
                <div
                  key={idx}
                  className='flex items-center gap-1 rounded-full bg-gray-200 px-3 py-1 text-xs'
                >
                  {tag}
                  <button
                    onClick={() => handleDeleteTag(idx)}
                    className='text-gray-500 hover:text-gray-700'
                  >
                    ✕
                  </button>
                </div>
              ))}
            </div>
          </div>
        </div>
        {isSaveDisabled && (
          <div className='mt-2 text-center text-sm text-red-500'>
            내용과 이미지가 모두 비어있어 저장할 수 없습니다.
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default MomentEditDialog;
