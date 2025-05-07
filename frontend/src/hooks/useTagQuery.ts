import { useInfiniteQuery, useQuery } from '@tanstack/react-query';

import { getUserTags } from '@/apis/tagApi';
import { mockGetUserTags, mockGetUserTagsInfinite } from '@/mock/mockTagApi';
import { UserTagsResponse } from '@/types/tag';

// 테스트를 위해 mock 데이터 사용 여부를 설정하는 상수
const useMockData = true;

export const useGetUserTags = () =>
  useQuery<UserTagsResponse>({
    queryKey: ['userTags'],
    queryFn: useMockData ? mockGetUserTags : getUserTags,
  });

// 태그 무한스크롤을 위한 훅 추가 (필요한 경우)
export const useGetUserTagsInfinite = (limit: number = 50) =>
  useInfiniteQuery({
    queryKey: ['userTags', 'infinite'],
    queryFn: ({ pageParam = 0 }) =>
      useMockData
        ? mockGetUserTagsInfinite({ page: pageParam, limit })
        : getUserTags(),
    initialPageParam: 0,
    getNextPageParam: (lastPage, allPages) => {
      const currentTotal = allPages.reduce(
        (sum, page) => sum + page.tags.length,
        0,
      );
      return currentTotal < lastPage.tagCount ? allPages.length : undefined;
    },
  });
