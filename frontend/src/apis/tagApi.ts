import { axiosInstance } from '@/apis/core';
import { UserTagsResponse } from '@/types/tag';

// 사용자 태그 조회 - 모달창에서 사용
export const getUserTags = async () => {
  const response = await axiosInstance.get<UserTagsResponse>('/api/user/tags');
  return response.data;
};
