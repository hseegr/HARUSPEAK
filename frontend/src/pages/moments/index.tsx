import { useMemo } from 'react';

import { useLocation } from 'react-router-dom';

import { useIntersectionObserver } from '@/hooks/useIntersectionObserver';
import { useGetMoments } from '@/hooks/useMomentQuery';
import { MomentContent } from '@/types/common';
import { MomentsResponse } from '@/types/moment';

import DateRangeDisplay from './components/DateRangeDisplay';
import MomentFrame from './components/MomentFrame';
import TagNameDisplay from './components/TagNameDisplay';

const Moments = () => {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);

  // useMemo를 사용하여 params 객체 메모이제이션
  const params = useMemo(
    () => ({
      limit: 30,
      startDate: searchParams.get('startDate') || undefined,
      endDate: searchParams.get('endDate') || undefined,
      userTags: searchParams.get('userTags') || undefined,
    }),
    [searchParams],
  );

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
        {(searchParams.get('startDate') || searchParams.get('endDate')) && (
          <DateRangeDisplay
            startDate={searchParams.get('startDate') || undefined}
            endDate={searchParams.get('endDate') || undefined}
          />
        )}

        {searchParams.get('userTags') && (
          <TagNameDisplay
            tagIds={searchParams.get('userTags')?.split(',') || []}
          />
        )}
      </div>

      <div className='grid grid-cols-1 gap-6'>
        {data?.pages?.map((page: MomentsResponse) =>
          page.data?.map((moment: MomentContent) => (
            <MomentFrame
              key={moment.momentId}
              momentId={moment.momentId}
              summaryId={moment.summaryId}
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

      {/* 데이터가 없을 때 메시지 표시 */}
      {!hasData && !isFetchingNextPage && (
        <div className='flex h-48 items-center justify-center text-gray-500'>
          순간 일기가 없습니다.
        </div>
      )}
    </div>
  );
};

export default Moments;
