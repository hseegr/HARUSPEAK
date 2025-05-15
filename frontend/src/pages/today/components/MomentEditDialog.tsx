import { useEffect, useState } from 'react';

import AutoTagGenerator from '@/components/AutoTagGenerator';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { useMomentEdit } from '@/hooks/useMomentEdit';
import { useTagForm } from '@/hooks/useTagForm';
import { MomentContent } from '@/types/common';

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
  const {
    editedMoment,
    isSaving,
    date,
    currentTime,
    isSaveDisabled,
    handleTimeChange,
    handleContentChange,
    handleDeleteImage,
    handleDeleteTag,
    handleSave,
    resetState,
  } = useMomentEdit(moment, () => onOpenChange(false));

  const [shouldResetTags, setShouldResetTags] = useState(false);

  const {
    register,
    handleSubmit,
    errors,
    reset,
    watch,
    tagError,
    setTagError,
    onSubmit,
    handleKeyPress,
  } = useTagForm({
    tags: editedMoment.tags,
    onTagAdd: tag => {
      editedMoment.tags = [...editedMoment.tags, tag];
    },
  });

  // Dialog가 열릴 때마다 상태 초기화
  useEffect(() => {
    if (open) {
      resetState();
      reset();
      setTagError('');
    }
  }, [open, resetState, reset, setTagError]);

  // 태그 초기화를 처리하는 useEffect
  useEffect(() => {
    if (shouldResetTags) {
      editedMoment.tags = [];
      setShouldResetTags(false);
    }
  }, [shouldResetTags, editedMoment]);

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
            <button
              onClick={handleCancel}
              className='text-haru-gray-5 hover:text-black'
            >
              취소
            </button>
          </DialogClose>
          <DialogTitle>오늘의 순간 수정</DialogTitle>
          <button
            onClick={handleSave}
            disabled={isSaving || isSaveDisabled}
            aria-label='순간 기록 수정 확정'
            className={`rounded-full px-3 py-1.5 text-sm ${
              isSaving || isSaveDisabled
                ? 'cursor-not-allowed bg-gray-300 text-haru-gray-5'
                : 'bg-haru-light-green text-white hover:bg-haru-green'
            }`}
          >
            {isSaving ? '...' : '저장'}
          </button>
        </DialogHeader>
        <div className='flex flex-col gap-2'>
          <div className='text-md'>{date}</div>
        </div>
        <div className='flex flex-col gap-4 rounded-xl bg-haru-beige p-3'>
          {/* 날짜 & 시간 */}
          <div>
            <input
              type='time'
              value={currentTime}
              onChange={handleTimeChange}
              className='rounded-full border bg-haru-yellow p-2 text-sm hover:cursor-pointer focus:outline-haru-green'
            />
          </div>

          {/* 이미지 리스트 */}
          {editedMoment.images.length > 0 && (
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
          )}

          {/* 순간 기록 */}
          <div className='flex flex-col gap-1'>
            <textarea
              value={editedMoment.content}
              onChange={e => handleContentChange(e.target.value)}
              rows={4}
              maxLength={1000}
              className='text-md max-h-[250px] min-h-[100px] w-full resize-none rounded-md border border-gray-300 p-2 focus:outline-haru-green'
              placeholder='순간의 기록을 입력하세요'
              style={{
                height: 'auto',
                overflowY: 'auto',
              }}
              onInput={e => {
                const target = e.target as HTMLTextAreaElement;
                target.style.height = 'auto';
                target.style.height = `${Math.min(target.scrollHeight, 250)}px`;
              }}
            />
          </div>

          {/* 태그 추가 */}
          <div className='flex flex-col'>
            <div>
              {errors.tag && errors.tag.type !== 'too_small' && (
                <div className='mb-2 text-xs text-red-500'>
                  {errors.tag?.message || tagError}
                </div>
              )}
            </div>
            <form
              onSubmit={handleSubmit(onSubmit)}
              className='mb-2 flex items-center gap-2'
            >
              <input
                {...register('tag')}
                onKeyDown={handleKeyPress}
                className={`w-[calc(100%-80px)] rounded-md border p-2 text-sm focus:outline-haru-green ${
                  errors.tag && errors.tag.type !== 'too_small'
                    ? 'border-red-500'
                    : 'border-gray-300'
                }`}
                placeholder={
                  editedMoment.tags.length >= 10
                    ? '태그 최대 개수 도달 (10개)'
                    : '태그 입력'
                }
                disabled={editedMoment.tags.length >= 10}
              />
              <button
                type='submit'
                disabled={editedMoment.tags.length >= 10 || !watch('tag')}
                className='whitespace-nowrap rounded-full bg-haru-green px-3 py-2 text-xs font-bold text-white disabled:bg-gray-300 disabled:text-gray-500'
              >
                태그 추가
              </button>
            </form>
            {/* 태그 관련 버튼 */}
            <div className='mb-2 flex flex-row-reverse justify-between'>
              <AutoTagGenerator
                moment={editedMoment}
                initialTags={editedMoment.tags}
                isToday={true}
                hideWhenDisabled={false}
                buttonStyle='simple'
              />
              <button
                onClick={() => setShouldResetTags(true)}
                className='text-sm font-bold text-red-600/80 hover:text-red-600 disabled:cursor-not-allowed disabled:text-gray-500 disabled:hover:text-gray-500'
                disabled={editedMoment.tags.length > 0 ? false : true}
              >
                태그 목록 초기화
              </button>
            </div>

            {/* 현재 태그 목록 */}
            <div className='flex flex-wrap gap-2'>
              {editedMoment.tags.map((tag, idx) => (
                <div
                  key={idx}
                  className='flex items-center gap-1 rounded-full bg-haru-gray-2 px-3 py-1 font-leeseyoon text-sm'
                >
                  {tag}
                  <button
                    onClick={() => handleDeleteTag(idx)}
                    className='text-haru-gray-5 hover:text-red-600'
                  >
                    ✕
                  </button>
                </div>
              ))}
            </div>
          </div>
        </div>
        {isSaveDisabled && (
          <div className='mt-2 text-center text-sm font-bold text-red-500'>
            내용과 이미지가 모두 비어있어 저장할 수 없습니다.
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default MomentEditDialog;
