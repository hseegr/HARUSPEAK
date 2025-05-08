import { useParams } from 'react-router-dom';

import { regenerateContent, regenerateImage } from '@/apis/diaryApi';
import MomentCard from '@/components/MomentCard';
import { useGetDiary } from '@/hooks/useDiaryQuery';

import ContentSummary from './components/ContentSummary';
import DiaryHeader from './components/DiaryHeader';
import ResetBtn from './components/ResetBtn';
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

  const handleImageReset = () => {
    regenerateImage(summaryId);
  };

  const handleContentReset = () => {
    regenerateContent(summaryId);
  };

  return (
    <div className='flex flex-col gap-5'>
      {/* 상단 */}
      <div>
        <div className=''>
          <DiaryHeader title={data.title} date={data.diaryDate} />
        </div>

        <div className='relative'>
          <Thumbnail summaryId={summaryId} />
          <div className='absolute bottom-2 right-2'>
            <ResetBtn
              generateCount={data.imageGenerateCount}
              onReset={handleImageReset}
              isDisabled={isPending}
              type='image'
            />
          </div>
        </div>
      </div>

      {/* 하루 요약 */}
      <div className='relative'>
        <ContentSummary summary={data.content} />
        <div className='absolute bottom-2 right-2'>
          <ResetBtn
            generateCount={data.contentGenerateCount}
            onReset={handleContentReset}
            isDisabled={isPending}
            type='content'
          />
        </div>
      </div>

      <div className='flex flex-col gap-4'>
        {/* 하단 - 순간 목록 */}
        {data.moments &&
          data.moments.length > 0 &&
          data.moments.map(moment => (
            <MomentCard key={moment.momentId} moment={moment} isToday={false} />
          ))}
      </div>
    </div>
  );
};

export default Diary;
