// Library 페이지에서 필터 관련 로직을 관리하는 훅
import { useCallback } from 'react';

import { useNavigate, useSearchParams } from 'react-router-dom';

interface FilterParams {
  startDate?: string;
  endDate?: string;
  userTags?: number[];
}

export const useLibraryFilters = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  // 검색 파라미터에서 필터 값 추출
  const startDate = searchParams.get('startDate') || undefined;
  const endDate = searchParams.get('endDate') || undefined;
  const userTags = searchParams.get('userTags') || undefined;

  // 태그 필터 적용 핸들러 - /moments 페이지로 이동하며 필터 적용
  const handleTagFilterApply = useCallback(
    (filters: FilterParams) => {
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

  return {
    filterParams: {
      startDate,
      endDate,
      userTags,
    },
    handleTagFilterApply,
    handleDateFilterApply,
  };
};
