import { useState } from 'react';

import { useFilterDialogs } from '@/hooks/useFilterDialogs';
import { useIntersectionObserver } from '@/hooks/useIntersectionObserver';
import { useLibraryDelete } from '@/hooks/useLibraryDelete';
import { useLibraryFilters } from '@/hooks/useLibraryFilters';
import { useGetLibrary } from '@/hooks/useLibraryQuery';

import DateRangeDisplay from '../moments/components/DateRangeDisplay';
import DateFilterBadge from './components/DateFilterBadge';
import DateFilterDialog from './components/DateFilterDialog';
import DeleteBtn from './components/DeleteBtn';
import DeleteConfirmDialog from './components/DeleteConfirmDialog';
import DiaryFrame from './components/DiaryFrame';
import FilterBadge from './components/FilterBadge';
import FilterDialog from './components/FilterDialog';

const Library = () => {
  // key 상태를 추가하여 Dialog 강제 리마운트에 사용
  const [dialogKey, setDialogKey] = useState(0);

  // 필터 관련 로직
  const { filterParams, handleTagFilterApply, handleDateFilterApply } =
    useLibraryFilters();
  const { startDate, endDate } = filterParams;

  // 다이얼로그 관련 로직
  const { dialogState, dialogActions } = useFilterDialogs();
  const { isFilterOpen, isDateFilterOpen } = dialogState;
  const {
    setIsFilterOpen,
    setIsDateFilterOpen,
    handleFilterClick,
    handleDateFilterClick,
  } = dialogActions;

  // 커스텀 onOpenChange 핸들러
  const handleDateFilterOpenChange = (open: boolean) => {
    setIsDateFilterOpen(open);
    if (!open) {
      // 다이얼로그가 닫힐 때 key 값을 증가시켜서 다음에 열릴 때 강제로 새로운 다이얼로그 생성
      setDialogKey(prev => prev + 1);
    }
  };

  // 커스텀 필터 적용 핸들러
  const handleCustomDateFilterApply = (filters: {
    startDate?: string;
    endDate?: string;
  }) => {
    handleDateFilterApply(filters);
    // 필터 적용 후에도 key 증가
    setDialogKey(prev => prev + 1);
  };

  // 선택 및 삭제 관련 로직
  const { selectionState, selectionActions } = useLibraryDelete();
  const { isSelectionMode, selectedIds, isDeleteDialogOpen, isDeleting } =
    selectionState;
  const {
    handleToggleSelection,
    handleSelect,
    handleReset,
    handleDeleteClick,
    handleConfirmDelete,
    setIsDeleteDialogOpen,
  } = selectionActions;

  // 무한 스크롤 데이터 가져오기
  const { data, fetchNextPage, hasNextPage, isFetchingNextPage } =
    useGetLibrary({
      limit: 30,
      startDate,
      endDate,
      userTags: filterParams.userTags,
    });

  // 교차 관찰자 설정
  const observerRef = useIntersectionObserver({
    onIntersect: fetchNextPage,
    enabled: hasNextPage && !isFetchingNextPage,
  });

  const hasData = (data?.pages?.[0]?.data?.length ?? 0) > 0;

  return (
    <div className='w-full'>
      <div className='fixed left-1/2 top-12 z-10 flex w-full max-w-96 -translate-x-1/2 items-center justify-between bg-white px-4 py-2'>
        <div className='flex items-center gap-2'>
          <FilterBadge onClick={handleFilterClick} />
          <DateFilterBadge onClick={handleDateFilterClick} />
        </div>
        <DeleteBtn
          isSelectionMode={isSelectionMode}
          onToggleSelection={handleToggleSelection}
          onDelete={handleDeleteClick}
          selectedCount={selectedIds.length}
          onReset={handleReset}
          isLoading={isDeleting}
        />
      </div>

      <div className='pt-12'>
        <div className='mb-4 px-4'>
          {(startDate || endDate) && (
            <DateRangeDisplay startDate={startDate} endDate={endDate} />
          )}
        </div>

        {/* 일기 목록 */}
        <div className='grid grid-cols-1 gap-6 px-4'>
          {data?.pages?.map(page =>
            page.data?.map(diary => (
              <DiaryFrame
                key={diary.summaryId}
                summaryId={diary.summaryId}
                diaryDate={diary.diaryDate}
                imageUrl={diary.imageUrl}
                title={diary.title}
                momentCount={diary.momentCount}
                isSelectionMode={isSelectionMode}
                isSelected={selectedIds.includes(diary.summaryId)}
                onSelect={handleSelect}
                isImageGenerating={diary.isImageGenerating}
              />
            )),
          )}
        </div>

        {/* 데이터가 없을 때 */}
        {hasData === false && !isFetchingNextPage && (
          <div className='py-4 text-center text-gray-500'>
            표시할 일기가 없습니다.
          </div>
        )}

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
      </div>

      {/* key 속성을 추가하여 다이얼로그 강제 리렌더링 */}
      <DateFilterDialog
        key={`date-filter-dialog-${dialogKey}`}
        open={isDateFilterOpen}
        onOpenChange={handleDateFilterOpenChange}
        onApply={handleCustomDateFilterApply}
        initialStartDate={startDate}
        initialEndDate={endDate}
      />

      <FilterDialog
        open={isFilterOpen}
        onOpenChange={setIsFilterOpen}
        onApply={handleTagFilterApply}
      />

      <DeleteConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        onConfirm={handleConfirmDelete}
        selectedCount={selectedIds.length}
        isDeleting={isDeleting}
      />
    </div>
  );
};

export default Library;
