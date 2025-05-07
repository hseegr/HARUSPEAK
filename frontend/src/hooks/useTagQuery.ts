import { useInfiniteQuery, useQuery } from '@tanstack/react-query';

import { getUserTags } from '@/apis/tagApi';
import { UserTagsResponse } from '@/types/tag';

export const useGetUserTags = () =>
  useQuery<UserTagsResponse>({
    queryKey: ['userTags'],
    queryFn: getUserTags,
  });

// 태그 무한스크롤을 위한 훅
// 참고: 현재 백엔드 API는 페이지네이션을 지원하지 않지만,
// 클라이언트에서 무한 스크롤을 구현하기 위한 로직
export const useGetUserTagsInfinite = () =>
  useInfiniteQuery({
    queryKey: ['userTags', 'infinite'],
    queryFn: () => getUserTags(),
    initialPageParam: 0,
    getNextPageParam: (lastPage, allPages) => {
      // 이미 불러온 태그의 총 개수
      const currentTotal = allPages.reduce(
        (sum, page) => sum + page.tags.length,
        0,
      );
      // 전체 태그 개수보다 현재 불러온 태그 개수가 적으면 다음 페이지 로드
      return currentTotal < lastPage.tagCount ? allPages.length : undefined;
    },
  });
