import { axiosInstance } from '@/apis/core';
import { GetMomentsParams } from '@/types/moment';
import { MomentsResponse } from '@/types/moment';

export const getMoments = async (
  params: GetMomentsParams,
): Promise<MomentsResponse> => {
  const response = await axiosInstance.get<MomentsResponse>('/api/moment', {
    params,
  });
  return response.data;
};

// 순간일기 상세조회(미사용)
export const getMoment = async (momentId: string) => {
  const response = await axiosInstance.get(`/api/moment/${momentId}`);
  return response.data;
};
