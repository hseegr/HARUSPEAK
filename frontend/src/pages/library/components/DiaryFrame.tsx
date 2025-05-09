import { useNavigate } from 'react-router-dom';

export interface DiaryFrameProps {
  summaryId: number;
  diaryDate: string;
  imageUrl: string;
  title: string;
  momentCount: number;
  isSelectionMode: boolean;
  isSelected: boolean;
  onSelect: (id: number) => void;
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
}: DiaryFrameProps) => {
  const navigate = useNavigate();

  // 선택 모드일 때 상세 페이지 이동 방지
  const handleClick = () => {
    if (!isSelectionMode) {
      navigate(`/diary/${summaryId}`);
    }
  };

  return (
    <div className='relative w-full'>
      {isSelectionMode && (
        <div className='absolute left-3 top-3 z-10'>
          <input
            type='checkbox'
            checked={isSelected}
            onChange={() => onSelect(summaryId)}
            className='h-5 w-5 rounded border-haru-gray-5'
          />
        </div>
      )}

      <div
        onClick={handleClick}
        className='flex w-full cursor-pointer flex-col gap-2 rounded-xl bg-haru-beige p-3'
      >
        <div className='absolute right-3 top-3 rounded-full rounded-bl-none bg-haru-yellow px-3 py-1 font-mont'>
          {diaryDate}
        </div>

        <div className='mt-8 flex flex-col'>
          <div className='font-mont'>{title}</div>
          <div className='font-mont'>{momentCount}개의 순간</div>
          <div>
            <img
              className='h-full w-full rounded-xl'
              src={imageUrl}
              alt='일기 썸네일'
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default DiaryFrame;
