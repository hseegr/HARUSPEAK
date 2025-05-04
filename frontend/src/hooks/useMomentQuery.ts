import { useInfiniteQuery, useQuery } from '@tanstack/react-query';

import { getMoment, getMoments } from '@/apis/momentApi';
import type { GetMomentsParams, MomentsResponse } from '@/apis/momentApi';

interface MomentResponse {
  momentId: number;
  momentTime: string;
  images: string[];
  content: string;
  tags: string[];
}

export const useGetMoments = (params: GetMomentsParams = {}) =>
  useInfiniteQuery<MomentsResponse>({
    queryKey: ['moments', params],
    queryFn: ({ pageParam }) =>
      getMoments({ ...params, before: pageParam as string | undefined }),
    getNextPageParam: lastPage =>
      lastPage.resInfo.hasMore ? lastPage.resInfo.nextCursor : undefined,
    initialPageParam: undefined,
  });

export const useGetMoment = (momentId: string) =>
  useQuery<MomentResponse>({
    queryKey: ['moment', momentId],
    queryFn: () => getMoment(momentId),
  });
