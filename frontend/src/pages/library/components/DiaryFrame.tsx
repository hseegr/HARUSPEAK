import { useNavigate } from 'react-router-dom';

interface DiaryFrameProps {
  summaryId: number;
  diaryDate: string;
  imageUrl: string;
  title: string;
  // content: string;
  momentCount: number;
}

const DiaryFrame = ({
  summaryId,
  diaryDate,
  imageUrl,
  title,
  // content, // 하루 요약 내용
  momentCount,
}: DiaryFrameProps) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/diary/${summaryId}`);
  };

  return (
    <div onClick={handleClick} className='cursor-pointer'>
      <div>{diaryDate}</div>
      <div>{title}</div>
      <img src={imageUrl} alt='일기 썸네일' />
      <div>{momentCount}개의 순간</div>
    </div>
  );
};

export default DiaryFrame;
