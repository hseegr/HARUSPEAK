import { useEffect, useState } from 'react';
import type { KeyboardEvent } from 'react';

import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';

import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { useMomentEdit } from '@/hooks/useMomentEdit';
import { MomentContent } from '@/types/common';

interface MomentEditDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  moment: MomentContent;
}

// 태그 입력을 위한 스키마 정의
const tagSchema = z.object({
  tag: z
    .string()
    .min(1, '태그를 입력해주세요')
    .max(10, '태그는 최대 10글자까지 작성 가능합니다')
    .regex(
      /^[a-zA-Z0-9가-힣_]*$/,
      '공백, _를 제외한 특수문자는 입력 불가합니다',
    ),
});

type TagFormData = z.infer<typeof tagSchema>;

const MomentEditDialog = ({
  open,
  onOpenChange,
  moment,
}: MomentEditDialogProps) => {
  const {
    editedMoment,
    setNewTag,
    isSaving,
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
  } = useMomentEdit(moment, () => onOpenChange(false));

  const [tagError, setTagError] = useState<string>('');

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<TagFormData>({
    resolver: zodResolver(tagSchema),
    defaultValues: {
      tag: '',
    },
  });

  // 태그 추가 핸들러
  const onSubmit = (data: TagFormData) => {
    if (editedMoment.tags.length >= 10) {
      setTagError('태그는 최대 10개까지 입력 가능합니다');
      return;
    }
    setNewTag(data.tag);
    handleAddTag();
    reset();
  };

  // 엔터 키 핸들러
  const handleKeyPress = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleSubmit(onSubmit)();
    }
  };

  // Dialog가 열릴 때마다 상태 초기화
  useEffect(() => {
    if (open) {
      resetState();
      reset();
      setTagError('');
    }
  }, [open, resetState, reset]);

  // 취소 버튼 클릭 시 원래 상태로 복구
  const handleCancel = () => {
    resetState();
    onOpenChange(false);
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
            aria-label='순간 기록 수정 확정'
            className={`rounded-full px-3 py-1.5 text-sm ${
              isSaving || isSaveDisabled
                ? 'cursor-not-allowed bg-gray-300 text-haru-gray-5'
                : 'bg-haru-green text-white'
            }`}
          >
            {isSaving ? '...' : '저장'}
          </button>
        </DialogHeader>
        <div className='flex flex-col gap-2'>
          <div className='text-end text-sm text-haru-gray-5'>{date}</div>
        </div>
        <div className='flex flex-col gap-4 rounded-xl bg-haru-beige p-3'>
          {/* 날짜 & 시간 */}
          <div>
            <input
              type='time'
              value={currentTime}
              onChange={handleTimeChange}
              className='rounded-full border bg-haru-yellow p-2 text-sm focus:outline-haru-green'
            />
          </div>

          {/* 이미지 리스트 */}
          <div className='grid grid-cols-3 gap-2'>
            {Array.from({ length: editedMoment.images.length }).map(
              (_, idx) => (
                <div
                  key={idx}
                  className='relative h-24 w-full rounded-lg bg-haru-gray-2'
                >
                  {editedMoment.images[idx] ? (
                    <>
                      <img
                        src={editedMoment.images[idx]}
                        alt={`image-${idx}`}
                        className='h-full w-full rounded-lg object-cover'
                      />
                      <button
                        onClick={() => handleDeleteImage(idx)}
                        className='absolute right-1 top-1 rounded-full bg-haru-black bg-opacity-50 p-0.5 px-1 text-xs text-white hover:text-red-500'
                      >
                        ✕
                      </button>
                    </>
                  ) : null}
                </div>
              ),
            )}
          </div>

          {/* 순간 기록 */}
          <div className='flex flex-col gap-1'>
            <textarea
              value={editedMoment.content}
              onChange={e => handleContentChange(e.target.value)}
              rows={4}
              className='w-full resize-none rounded-md border border-gray-300 p-2 text-sm focus:outline-[#41644A]'
              placeholder='순간의 기록을 입력하세요'
            />
          </div>

          {/* 태그 추가 */}
          <div className='flex flex-col gap-2'>
            <form onSubmit={handleSubmit(onSubmit)} className='flex gap-2'>
              <input
                {...register('tag')}
                onKeyDown={handleKeyPress}
                className='flex-1 rounded-md border border-gray-300 p-2 text-sm focus:outline-[#41644A]'
                placeholder='태그 입력'
                disabled={editedMoment.tags.length >= 10}
              />
              <button
                type='submit'
                disabled={editedMoment.tags.length >= 10}
                className='rounded-full bg-[#41644A] px-3 py-1.5 text-sm text-white disabled:bg-gray-300'
              >
                태그 추가
              </button>
            </form>
            {(errors.tag || tagError) && (
              <div className='text-sm text-red-500'>
                {errors.tag?.message || tagError}
              </div>
            )}

            {/* 현재 태그 목록 */}
            <div className='flex flex-wrap gap-2'>
              {editedMoment.tags.map((tag, idx) => (
                <div
                  key={idx}
                  className='flex items-center gap-1 rounded-full bg-gray-200 px-3 py-1 text-xs'
                >
                  {tag}
                  <button
                    onClick={() => handleDeleteTag(idx)}
                    className='text-gray-500 hover:text-red-600'
                  >
                    ✕
                  </button>
                </div>
              ))}
            </div>
            <button
              onClick={() => {
                editedMoment.tags = [];
              }}
              className='mt-2 text-sm text-red-500'
            >
              태그 목록 초기화
            </button>
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
