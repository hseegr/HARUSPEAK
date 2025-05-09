import { useCallback, useState } from 'react';

import { useNavigate, useSearchParams } from 'react-router-dom';

import { useIntersectionObserver } from '@/hooks/useIntersectionObserver';
import { useDeleteDiary, useGetLibrary } from '@/hooks/useLibraryQuery';

import DateFilterBadge from './components/DateFilterBadge';
import DateFilterDialog from './components/DateFilterDialog';
import DeleteBtn from './components/DeleteBtn';
import DeleteConfirmDialog from './components/DeleteConfirmDialog';
import DiaryFrame from './components/DiaryFrame';
import FilterBadge from './components/FilterBadge';
import FilterDialog from './components/FilterDialog';

const Library = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  // 검색 파라미터에서 필터 값 추출
  const startDate = searchParams.get('startDate') || undefined;
  const endDate = searchParams.get('endDate') || undefined;
  const userTags = searchParams.get('userTags') || undefined;

  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [isDateFilterOpen, setIsDateFilterOpen] = useState(false);
  const [isSelectionMode, setIsSelectionMode] = useState(false);
  const [selectedIds, setSelectedIds] = useState<number[]>([]);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);

  // 무한 스크롤 데이터 가져오기
  const { data, fetchNextPage, hasNextPage, isFetchingNextPage } =
    useGetLibrary({
      limit: 30,
      startDate,
      endDate,
      userTags,
    });

  const deleteDiary = useDeleteDiary();

  // 교차 관찰자 설정
  const observerRef = useIntersectionObserver({
    onIntersect: fetchNextPage,
    enabled: hasNextPage && !isFetchingNextPage,
  });

  const handleFilterClick = useCallback(() => {
    setIsFilterOpen(true);
  }, []);

  // 날짜 필터 클릭 핸들러 추가
  const handleDateFilterClick = useCallback(() => {
    setIsDateFilterOpen(true);
  }, []);

  // 태그 필터 적용 핸들러 - /moments 페이지로 이동하며 필터 적용
  const handleTagFilterApply = useCallback(
    (filters: {
      startDate?: string;
      endDate?: string;
      userTags?: number[];
    }) => {
      const newSearchParams = new URLSearchParams();

      if (filters.startDate) {
        newSearchParams.set('startDate', filters.startDate);
      }
      if (filters.endDate) {
        newSearchParams.set('endDate', filters.endDate);
      }
      if (filters.userTags?.length) {
        newSearchParams.set('userTags', filters.userTags.join(','));
      }

      // moments 페이지로 이동하며 필터 적용
      navigate(`/moments?${newSearchParams.toString()}`);
    },
    [navigate],
  );

  // 날짜 필터 적용 핸들러 - 현재 Library 페이지에서 필터 적용
  const handleDateFilterApply = useCallback(
    (filters: { startDate?: string; endDate?: string }) => {
      // 기존 파라미터 유지하면서 새로운 날짜 필터 적용
      const newSearchParams = new URLSearchParams(searchParams.toString());

      if (filters.startDate) {
        newSearchParams.set('startDate', filters.startDate);
      } else {
        newSearchParams.delete('startDate');
      }

      if (filters.endDate) {
        newSearchParams.set('endDate', filters.endDate);
      } else {
        newSearchParams.delete('endDate');
      }

      // 현재 페이지에 필터 적용 (라우트 변경 없이 쿼리 파라미터만 업데이트)
      navigate({ search: newSearchParams.toString() }, { replace: true });
    },
    [navigate, searchParams],
  );

  const handleToggleSelection = useCallback(() => {
    setIsSelectionMode(prev => !prev);
    setSelectedIds([]);
  }, []);

  const handleSelect = useCallback((summaryId: number) => {
    setSelectedIds(prev =>
      prev.includes(summaryId)
        ? prev.filter(id => id !== summaryId)
        : [...prev, summaryId],
    );
  }, []);

  const handleReset = useCallback(() => {
    setSelectedIds([]);
  }, []);

  // 삭제 버튼 클릭 시 Dialog 열기
  const handleDeleteClick = useCallback(() => {
    if (selectedIds.length > 0) {
      setIsDeleteDialogOpen(true);
    }
  }, [selectedIds]);

  // 실제 삭제 처리 (개별 요청)
  const handleConfirmDelete = useCallback(async () => {
    if (selectedIds.length === 0) return;

    try {
      setIsDeleting(true);
      console.log('삭제 시작:', selectedIds);

      // 각 일기를 순차적으로 개별 삭제 (Promise.all 대신 for문 사용)
      for (const id of selectedIds) {
        try {
          await deleteDiary(id);
          console.log(`일기 ${id} 삭제 성공`);
        } catch (error) {
          console.error(`일기 ${id} 삭제 실패:`, error);
        }
      }
      setIsSelectionMode(false);
      setSelectedIds([]);
      setIsDeleteDialogOpen(false);
    } catch (error) {
      console.error('일기 삭제 중 오류 발생:', error);
    } finally {
      setIsDeleting(false);
    }
  }, [deleteDiary, selectedIds]);

  const hasData = (data?.pages?.[0]?.data?.length ?? 0) > 0;

  return (
    <div className='w-full'>
      <div className='fixed left-1/2 top-12 z-10 flex w-full max-w-96 -translate-x-1/2 items-center justify-between bg-white px-4 py-2'>
        {/* 필터 배지들을 플렉스 컨테이너로 묶음 */}
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

      <DateFilterDialog
        open={isDateFilterOpen}
        onOpenChange={setIsDateFilterOpen}
        onApply={handleDateFilterApply}
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
      />
    </div>
  );
};

export default Library;
