import { useState } from 'react';

import { useGetMoments } from '@/hooks/useMomentQuery';

import DeleteBtn from './components/DeleteBtn';
import DiaryFrame from './components/DiaryFrame';
import FilterBadge from './components/FilterBadge';
import FilterModal from './components/FilterModal';

interface FilterState {
  startDate?: string;
  endDate?: string;
  userTags?: string;
}

// 내 서재에서 하루일기 모아보기
const Library = () => {
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [filters, setFilters] = useState<FilterState>({});
  const { data, fetchNextPage, hasNextPage, isFetchingNextPage } =
    useGetMoments({
      limit: 30,
      ...filters,
    });

  const handleFilterApply = (newFilters: FilterState) => {
    setFilters(newFilters);
    setIsFilterOpen(false);
  };

  const handleLoadMore = () => {
    if (!isFetchingNextPage && hasNextPage) {
      fetchNextPage();
    }
  };

  return (
    <div className='container mx-auto px-4 py-8'>
      <div className='mb-6 flex items-center justify-between'>
        <FilterBadge onClick={() => setIsFilterOpen(true)} />
        <DeleteBtn />
      </div>

      <div className='grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3'>
        {data?.pages.map(page =>
          page.data.map(moment => (
            <DiaryFrame
              key={moment.momentId}
              summaryId={moment.summaryId}
              momentId={moment.momentId}
              diaryDate={moment.momentTime}
              imageUrl={moment.images[0]}
              content={moment.content}
              tags={moment.tags}
            />
          )),
        )}
      </div>

      {hasNextPage && (
        <button
          onClick={handleLoadMore}
          className='mt-8 w-full rounded-lg bg-gray-100 py-3 text-gray-700 hover:bg-gray-200'
          disabled={isFetchingNextPage}
        >
          {isFetchingNextPage ? '불러오는 중...' : '더 보기'}
        </button>
      )}

      {isFilterOpen && (
        <FilterModal
          onApply={handleFilterApply}
          onClose={() => setIsFilterOpen(false)}
        />
      )}
    </div>
  );
};

export default Library;
