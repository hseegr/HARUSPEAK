import { useInfiniteQuery, useQuery } from '@tanstack/react-query';

import { getMoment, getMoments } from '@/apis/momentApi';
import {
  GetMomentsParams,
  MomentResponse,
  MomentsResponse,
} from '@/types/moment';

export const useGetMoments = (params: GetMomentsParams = {}) =>
  useInfiniteQuery<MomentsResponse>({
    queryKey: ['moments', params],
    queryFn: ({ pageParam }) =>
      getMoments({ ...params, before: pageParam as string | undefined }),
    getNextPageParam: lastPage =>
      lastPage.resInfo.hasMore ? lastPage.resInfo.nextCursor : undefined,
    initialPageParam: undefined,
  });

// 순간일기 상세조회(미사용)
export const useGetMoment = (momentId: string) =>
  useQuery<MomentResponse>({
    queryKey: ['moment', momentId],
    queryFn: () => getMoment(momentId),
    enabled: !!momentId,
  });
