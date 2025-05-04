import { useLocation } from 'react-router-dom';

import { MomentsResponse } from '@/apis/momentApi';
import { useIntersectionObserver } from '@/hooks/useIntersectionObserver';
import { useGetMoments } from '@/hooks/useMomentQuery';

import MomentFrame from './components/MomentFrame';

export interface Moment {
  summaryId: number;
  momentId: number;
  momentTime: string;
  imageCount: number;
  images: string[];
  content: string;
  tagCount: number;
  tags: string[];
}

const Moments = () => {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);

  const params = {
    limit: 30,
    startDate: searchParams.get('startDate') || undefined,
    endDate: searchParams.get('endDate') || undefined,
    userTags: searchParams.get('userTags') || undefined,
  };

  const { data, fetchNextPage, hasNextPage, isFetchingNextPage } =
    useGetMoments(params);

  const observerRef = useIntersectionObserver({
    onIntersect: fetchNextPage,
    enabled: hasNextPage && !isFetchingNextPage,
  });

  // 타입 추론 최적화 고민
  const hasData = Boolean(data?.pages?.[0]?.data?.length);

  return (
    <div className='container mx-auto px-4 py-8'>
      <div className='mb-6'>
        <h1 className='text-2xl font-bold'>순간 일기</h1>
        {searchParams.get('userTags') && (
          <div className='mt-2 text-sm text-gray-500'>
            선택된 태그: {searchParams.get('userTags')?.split(',').join(', ')}
          </div>
        )}
      </div>

      <div className='grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3'>
        {data?.pages?.map((page: MomentsResponse) =>
          page.data?.map((moment: Moment) => (
            <MomentFrame
              key={moment.momentId}
              momentId={moment.momentId}
              momentTime={moment.momentTime}
              images={moment.images}
              content={moment.content}
              tags={moment.tags}
            />
          )),
        )}
      </div>

      {/* Intersection Observer 타겟 요소 */}
      <div ref={observerRef} className='h-10' />

      {/* 로딩 상태 표시 */}
      {isFetchingNextPage && (
        <div className='flex justify-center py-4'>
          <div className='h-8 w-8 animate-spin rounded-full border-4 border-gray-200 border-t-blue-500' />
        </div>
      )}

      {/* 더 이상 데이터가 없을 때 */}
      {!hasNextPage && hasData && (
        <div className='py-4 text-center text-gray-500'>
          모든 순간 일기를 불러왔습니다.
        </div>
      )}
    </div>
  );
};

export default Moments;
