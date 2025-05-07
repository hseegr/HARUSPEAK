import { axiosInstance } from '@/apis/core';
import { GetMomentsParams } from '@/types/moment';
import { MomentsResponse } from '@/types/moment';

// 순간일기 목록조회
export const getMoments = async (params: GetMomentsParams) => {
  const response = await axiosInstance.get('/api/moment', { params });
  return response.data as MomentsResponse;
};

// 순간일기 상세조회(미사용)
export const getMoment = async (momentId: string) => {
  const response = await axiosInstance.get(`/api/moment/${momentId}`);
  return response.data;
};
