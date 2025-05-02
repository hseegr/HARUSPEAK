import { useParams } from 'react-router-dom';

import MomentCard from '@/components/MomentCard';

import ContentSummary from './components/ContentSummary';
import DiaryHeader from './components/DiaryHeader';
import Thumbnail from './components/Thumbnail';

// 일기 1개 상세 보기
const Diary = () => {
  const { summaryId } = useParams();

  if (!summaryId) {
    return null;
  }

  return (
    <div>
      <DiaryHeader />
      <Thumbnail summaryId={summaryId} />
      <ContentSummary />

      <MomentCard />
    </div>
  );
};
export default Diary;
