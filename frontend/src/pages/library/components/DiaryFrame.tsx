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
        className='flex w-full cursor-pointer flex-col gap-2 rounded-xl bg-haru-beige p-3'
      >
        {/* 상단 부분: 날짜는 왼쪽, 체크박스는 오른쪽으로 이동 */}
        <div className='flex items-center justify-between'>
          <div className='rounded-full rounded-br-none bg-haru-yellow px-3 py-1 font-mont'>
            {diaryDate}
          </div>

          {/* 선택 모드일 때만 체크박스 표시 */}
          {isSelectionMode && (
            <div className='z-10 pr-1'>
              <Checkbox
                checked={isSelected}
                onCheckedChange={handleCheckedChange}
                className='h-5 w-5 border-haru-gray-5 bg-white data-[state=checked]:border-haru-green data-[state=checked]:bg-haru-green'
                onClick={e => e.stopPropagation()}
                id={`checkbox-${summaryId}`}
              />
            </div>
          )}
        </div>

        <div className='mt-2 flex flex-col'>
          <div className='font-mont'>{title}</div>
          <div className='font-mont'>{momentCount}개의 순간</div>
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
