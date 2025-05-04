import { useNavigate } from 'react-router-dom';

export interface DiaryFrameProps {
  summaryId: number;
  diaryDate: string;
  imageUrl: string;
  title: string;
  // content: string; // 하루 AI 요약 내용을 props로 할 필요가?
  momentCount: number;
}

const DiaryFrame = ({
  summaryId,
  diaryDate,
  imageUrl,
  title,
  // content,
  momentCount,
}: DiaryFrameProps) => {
  const navigate = useNavigate();

  // 클릭 시 하루 일기 상세 조회 페이지로 이동
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
