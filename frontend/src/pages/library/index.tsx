import { useState } from 'react';

import { useNavigate } from 'react-router-dom';

import { useIntersectionObserver } from '@/hooks/useIntersectionObserver';
import { useDeleteDiaries, useGetLibrary } from '@/hooks/useLibraryQuery';

import DeleteBtn from './components/DeleteBtn';
import DeleteConfirmDialog from './components/DeleteConfirmDialog';
import DiaryFrame from './components/DiaryFrame';
import FilterBadge from './components/FilterBadge';
import FilterDialog from './components/FilterDialog';

const Library = () => {
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [isSelectionMode, setIsSelectionMode] = useState(false);
  const [selectedIds, setSelectedIds] = useState<number[]>([]);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const navigate = useNavigate();

  const { data, fetchNextPage, hasNextPage, isFetchingNextPage } =
    useGetLibrary({
      limit: 30,
    });

  const observerRef = useIntersectionObserver({
    onIntersect: fetchNextPage,
    enabled: hasNextPage && !isFetchingNextPage,
  });

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

    navigate(`/moments?${searchParams.toString()}`);
  };

  const handleToggleSelection = () => {
    setIsSelectionMode(!isSelectionMode);
    setSelectedIds([]);
  };

  const handleSelect = (summaryId: number) => {
    setSelectedIds(prev =>
      prev.includes(summaryId)
        ? prev.filter(id => id !== summaryId)
        : [...prev, summaryId],
    );
  };

  // 삭제할 일기 선택 초기화
  const handleReset = () => {
    setSelectedIds([]);
  };
  const deleteDiaries = useDeleteDiaries();

  // 삭제 버튼 클릭 시 Dialog 열기
  const handleDeleteClick = () => {
    if (selectedIds.length > 0) {
      setIsDeleteDialogOpen(true);
    }
  };

  // 실제 삭제 처리 (개별 요청)
  const handleConfirmDelete = async () => {
    // 각 일기를 개별적으로 삭제 요청
    for (const id of selectedIds) {
      await deleteDiaries([id]);
    }
    setIsSelectionMode(false);
    setSelectedIds([]);
    setIsDeleteDialogOpen(false);
  };

  const hasData = (data?.pages?.[0]?.data?.length ?? 0) > 0;

  return (
    <div className='w-full'>
      {/* 고정 헤더 아래에 위치할 컨트롤 바 */}
      <div className='fixed left-1/2 top-12 z-10 flex w-full max-w-96 -translate-x-1/2 items-center justify-between bg-white px-4 py-2'>
        <FilterBadge onClick={handleFilterClick} />
        <DeleteBtn
          isSelectionMode={isSelectionMode}
          onToggleSelection={handleToggleSelection}
          onDelete={handleDeleteClick}
          selectedCount={selectedIds.length}
          onReset={handleReset}
        />
      </div>

      {/* 컨트롤 바만큼 상단 여백 추가 */}
      <div className='pt-12'>
        <div className='grid grid-cols-1 gap-6'>
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
      </div>

      <FilterDialog
        open={isFilterOpen}
        onOpenChange={setIsFilterOpen}
        onApply={handleFilterApply}
      />

      <DeleteConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        onConfirm={handleConfirmDelete}
        selectedCount={selectedIds.length}
      />
    </div>
  );
};

export default Library;
