import { useParams } from 'react-router-dom';

import { useGetDiary } from '@/hooks/useDiaryQuery';

import ContentSummary from './components/ContentSummary';
import DiaryHeader from './components/DiaryHeader';
import Thumbnail from './components/Thumbnail';

// 일기 1개 상세 보기
const Diary = () => {
  const { summaryId } = useParams();
  const { data, isPending, isError } = useGetDiary(summaryId || '');

  if (!summaryId) {
    return <div>일기를 찾을 수 없습니다.</div>;
  }

  if (isPending) {
    return <div>일기를 불러오는 중입니다...</div>;
  }

  if (isError) {
    return <div>일기를 불러오는 중 오류가 발생했습니다.</div>;
  }

  if (!data) {
    return <div>일기를 찾을 수 없습니다.</div>;
  }

  return (
    <div>
      <DiaryHeader title={data.title} date={data.diaryDate} />
      <Thumbnail summaryId={summaryId} />
      <ContentSummary />

      {/* <MomentCard /> */}
    </div>
  );
};

export default Diary;
