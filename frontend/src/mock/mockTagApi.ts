import { UserTagsResponse } from '@/types/tag';

import { mockUserTags } from './tagMock';

export const mockGetUserTags = async (): Promise<UserTagsResponse> => {
  // API 응답을 시뮬레이션하기 위해 약간의 지연을 추가
  await new Promise(resolve => setTimeout(resolve, 500));

  return {
    tags: mockUserTags,
    tagCount: mockUserTags.length,
  };
};

export const mockGetUserTagsInfinite = async (params: {
  page: number;
  limit: number;
}): Promise<UserTagsResponse> => {
  await new Promise(resolve => setTimeout(resolve, 500));

  const start = params.page * params.limit;
  const end = start + params.limit;
  const paginatedTags = mockUserTags.slice(start, end);

  return {
    tags: paginatedTags,
    tagCount: mockUserTags.length,
  };
};
