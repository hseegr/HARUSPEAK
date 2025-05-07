import { useEffect } from 'react';

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

const MomentEditDialog = ({
  open,
  onOpenChange,
  moment,
}: MomentEditDialogProps) => {
  const {
    editedMoment,
    newTag,
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

  // Dialog가 열릴 때마다 상태 초기화
  useEffect(() => {
    if (open) {
      resetState();
    }
  }, [open, resetState]); // resetState가 useCallback으로 memoized되어 있어서 안전함

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
            className={`rounded-full px-3 py-1.5 text-sm ${
              isSaving || isSaveDisabled
                ? 'cursor-not-allowed bg-gray-300 text-haru-gray-5'
                : 'bg-haru-green text-white'
            }`}
          >
            {isSaving ? '저장 중...' : '저장'}
          </button>
        </DialogHeader>
        <div className='flex flex-col gap-2'>
          <div className='text-sm text-end text-haru-gray-5'>{date}</div>
        </div>
        <div className='flex flex-col gap-4 p-3 rounded-xl bg-haru-beige'>
          {/* 날짜 & 시간 */}
          <div>
            <input
              type='time'
              value={currentTime}
              onChange={handleTimeChange}
              className='p-2 text-sm border rounded-full bg-haru-yellow focus:outline-haru-green'
            />
          </div>

          {/* 이미지 리스트 */}
          <div className='grid grid-cols-3 gap-2'>
            {Array.from({ length: editedMoment.images.length }).map(
              (_, idx) => (
                <div
                  key={idx}
                  className='relative w-full h-24 rounded-lg bg-haru-gray-2'
                >
                  {editedMoment.images[idx] ? (
                    <>
                      <img
                        src={editedMoment.images[idx]}
                        alt={`image-${idx}`}
                        className='object-cover w-full h-full rounded-lg'
                      />
                      <button
                        onClick={() => handleDeleteImage(idx)}
                        className='absolute right-1 top-1 rounded-full bg-haru-black bg-opacity-50 p-0.5 px-1 text-xs text-white'
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
              {editedMoment.tags.map((tag, idx) => (
                <div
                  key={idx}
                  className='flex items-center gap-1 px-3 py-1 text-xs bg-gray-200 rounded-full'
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
          <div className='mt-2 text-sm text-center text-red-500'>
            내용과 이미지가 모두 비어있어 저장할 수 없습니다.
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default MomentEditDialog;
