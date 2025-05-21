import { useNavigate } from 'react-router-dom';

import ImageSkeleton from '@/components/ImageSkeleton';
import { Checkbox } from '@/components/ui/checkbox';

export interface DiaryFrameProps {
  summaryId: number;
  diaryDate: string;
  imageUrl: string;
  title: string;
  momentCount: number;
  isSelectionMode: boolean;
  isSelected: boolean;
  onSelect: (id: number) => void;
  isImageGenerating?: boolean;
}

const DiaryFrame = ({
  summaryId,
  diaryDate,
  imageUrl,
  title,
  momentCount,
  isSelectionMode,
  isSelected,
  onSelect,
  isImageGenerating,
}: DiaryFrameProps) => {
  const navigate = useNavigate();

  // 선택 모드일 때 상세 페이지 이동 방지
  const handleClick = () => {
    if (!isSelectionMode) {
      navigate(`/diary/${summaryId}`);
    }
  };

  // shadcn 체크박스 onCheckedChange 핸들러
  const handleCheckedChange = () => {
    onSelect(summaryId);
  };

  return (
    <div className='relative w-full'>
      <div
        onClick={handleClick}
        className='mt-2 flex w-full cursor-pointer flex-col gap-1 rounded-xl bg-haru-beige p-3'
      >
        <div className='flex items-center'>
          <div className='rounded-full rounded-bl-none bg-haru-yellow px-3 py-1 font-mont'>
            {diaryDate}
          </div>
          <div className='px-3 font-leeseyoon text-haru-gray-5'>
            {momentCount}개의 순간
          </div>

          {/* 선택 모드일 때만 체크박스 표시 */}
          {isSelectionMode && (
            <div className='pr-1'>
              <Checkbox
                checked={isSelected}
                onCheckedChange={handleCheckedChange}
                className='absolute right-3 top-7 h-5 w-5 -translate-y-1/2 border-haru-gray-5 bg-white data-[state=checked]:border-haru-green data-[state=checked]:bg-haru-green'
                onClick={e => e.stopPropagation()}
                disabled={isImageGenerating}
                id={`checkbox-${summaryId}`}
              />
            </div>
          )}
        </div>

        <div className='flex flex-col'>
          <div
            className='m-1 font-leeseyoon text-lg'
            style={{ whiteSpace: 'pre-wrap', wordBreak: 'break-all' }}
          >
            {title}
          </div>
          <div className='w-full overflow-hidden'>
            {isImageGenerating || !imageUrl ? (
              <ImageSkeleton />
            ) : (
              <img
                className='h-full w-full rounded-xl'
                src={imageUrl}
                alt='일기 썸네일'
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DiaryFrame;
