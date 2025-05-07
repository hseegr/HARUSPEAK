import { useNavigate } from 'react-router-dom';

export interface DiaryFrameProps {
  summaryId: number;
  diaryDate: string;
  imageUrl: string;
  title: string;
  // content: string; // 하루 AI 요약 내용을 props로 할 필요가?
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
  // content,
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
    <div className='relative'>
      {isSelectionMode && (
        <div className='absolute left-2 top-2 z-10'>
          <input
            type='checkbox'
            checked={isSelected}
            onChange={() => onSelect(summaryId)}
            className='h-5 w-5 rounded border-gray-300'
          />
        </div>
      )}
      <div onClick={handleClick} className='cursor-pointer'>
        <div>{diaryDate}</div>
        <div>{title}</div>
        <img src={imageUrl} alt='일기 썸네일' />
        <div>{momentCount}개의 순간</div>
      </div>
    </div>
  );
};

export default DiaryFrame;
