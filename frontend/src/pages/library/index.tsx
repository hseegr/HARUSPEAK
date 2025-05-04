import { useState } from 'react';

import { useNavigate } from 'react-router-dom';

import { useIntersectionObserver } from '@/hooks/useIntersectionObserver';
import { useGetLibrary } from '@/hooks/useLibraryQuery';

import DeleteBtn from './components/DeleteBtn';
import DiaryFrame from './components/DiaryFrame';
import FilterBadge from './components/FilterBadge';
import FilterModal from './components/FilterModal';

// 내 서재에서 하루일기 모아보기
const Library = () => {
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const navigate = useNavigate();

  const { data, fetchNextPage, hasNextPage, isFetchingNextPage } =
    useGetLibrary({
      limit: 30,
    });

  // Intersection Observer를 사용한 자동 무한 스크롤
  const observerRef = useIntersectionObserver({
    onIntersect: fetchNextPage,
    enabled: hasNextPage && !isFetchingNextPage,
  });

  // 순간 일기(moment) 필터링 모달 열기
  const handleFilterClick = () => {
    setIsFilterOpen(true);
  };

  const handleFilterApply = (filters: {
    startDate?: string;
    endDate?: string;
    userTags?: string[];
  }) => {
    const searchParams = new URLSearchParams();

    if (filters.startDate) {
      searchParams.set('startDate', filters.startDate);
    }
    if (filters.endDate) {
      searchParams.set('endDate', filters.endDate);
    }
    if (filters.userTags?.length) {
      searchParams.set('userTags', filters.userTags.join(','));
    }

    // 필터가 적용된 Moments 페이지로 이동
    navigate(`/moments?${searchParams.toString()}`);
  };

  const hasData = (data?.pages?.[0]?.data?.length ?? 0) > 0;

  return (
    <div className='container mx-auto px-4 py-8'>
      <div className='mb-6 flex items-center justify-between'>
        <FilterBadge onClick={handleFilterClick} />
        <DeleteBtn />
      </div>

      <div className='grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3'>
        {data?.pages?.map(page =>
          page.data?.map(diary => (
            <DiaryFrame
              key={diary.summaryId}
              summaryId={diary.summaryId}
              diaryDate={diary.diaryDate}
              imageUrl={diary.imageUrl}
              title={diary.title}
              momentCount={diary.momentCount}
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
          모든 일기를 불러왔습니다.
        </div>
      )}

      {/* 순간 일기 필터링 모달 */}
      <FilterModal
        open={isFilterOpen}
        onOpenChange={setIsFilterOpen}
        onApply={handleFilterApply}
      />
    </div>
  );
};

export default Library;
