import { axiosInstance } from './core';

interface GetMomentsParams {
  before?: string;
  limit?: number;
  startDate?: string;
  endDate?: string;
  userTags?: string;
}

interface Moment {
  summaryId: number;
  momentId: number;
  momentTime: string;
  imageCount: number;
  images: string[];
  content: string;
  tagCount: number;
  tags: string[];
}

interface MomentsResponse {
  data: Moment[];
  resInfo: {
    dataCount: number;
    nextCursor: string;
    hasMore: boolean;
  };
}

// 순간일기 상세조회
export const getMoment = async (momentId: string) => {
  const response = await axiosInstance.get(`/api/moment/${momentId}`);
  return response.data;
};

// 순간일기 목록조회
export const getMoments = async (params?: GetMomentsParams) => {
  const response = await axiosInstance.get('/api/moment', { params });
  return response.data as MomentsResponse;
};
