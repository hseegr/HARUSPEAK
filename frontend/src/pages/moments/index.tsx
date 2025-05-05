import { useEffect, useMemo, useState } from 'react';

import { useLocation } from 'react-router-dom';

import { MomentsResponse } from '@/apis/momentApi';
import { useIntersectionObserver } from '@/hooks/useIntersectionObserver';
import { useGetMoments } from '@/hooks/useMomentQuery';
import { getMockMoments } from '@/mock/mockMomentData';

// Mock 데이터 import

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

  // Mock 데이터 상태 추가
  const [mockData, setMockData] = useState<MomentsResponse | null>(null);

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

  // Mock 데이터 로드 (실제 환경에서는 제거)
  useEffect(() => {
    // API가 동작하지 않는 개발 환경에서만 Mock 데이터 사용
    if (!mockData) {
      // 한 번만 로드하도록 조건 추가
      try {
        console.log('Mock 데이터 로드 시도');
        const mockResponse = getMockMoments(params);
        setMockData(mockResponse);
      } catch (error) {
        console.error('Mock 데이터 로드 실패:', error);
      }
    }
  }, []); // 빈 의존성 배열로 컴포넌트 마운트 시에만 실행

  const observerRef = useIntersectionObserver({
    onIntersect: fetchNextPage,
    enabled: hasNextPage && !isFetchingNextPage,
  });

  // 타입 추론 최적화 고민
  const hasData = Boolean(
    data?.pages?.[0]?.data?.length || mockData?.data?.length,
  );

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

      <div className='grid grid-cols-1 gap-6'>
        {/* 실제 API 데이터 처리 */}
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

        {/* Mock 데이터 처리 (실제 API 데이터가 없는 경우) */}
        {!data &&
          mockData?.data?.map((moment: Moment) => (
            <MomentFrame
              key={moment.momentId}
              momentId={moment.momentId}
              momentTime={moment.momentTime}
              images={moment.images}
              content={moment.content}
              tags={moment.tags}
            />
          ))}
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
