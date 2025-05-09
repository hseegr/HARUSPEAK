import { useInfiniteQuery } from '@tanstack/react-query';

import { getMoments } from '@/apis/momentApi';
import { GetMomentsParams, MomentsResponse } from '@/types/moment';

export const useGetMoments = (params: GetMomentsParams = {}) =>
  useInfiniteQuery<MomentsResponse>({
    // params 객체 대신 개별 파라미터를 명시적으로 추가하여
    // 필터 변경 시 자동 재요청 되도록 수정
    queryKey: [
      'moments',
      params.limit,
      params.startDate,
      params.endDate,
      params.userTags,
    ],
    queryFn: ({ pageParam }) =>
      getMoments({ ...params, before: pageParam as string | undefined }),
    getNextPageParam: lastPage =>
      lastPage.resInfo.hasMore ? lastPage.resInfo.nextCursor : undefined,
    initialPageParam: undefined,
    // 불필요한 리렌더링 방지
    structuralSharing: true,
  });
