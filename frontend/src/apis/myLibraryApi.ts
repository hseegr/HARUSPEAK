import { axiosInstance } from '@/apis/core';

interface GetLibraryParams {
  before?: string;
  limit?: number;
  startDate?: string;
  endDate?: string;
}

// 하루 요약 일기 모아보기 - 최신순/검색
export const getLibrary = async (params?: GetLibraryParams) => {
  const response = await axiosInstance.get('/api/summary', { params });
  return response.data;
};

// 하루일기 수정(오늘 제외)
export const editDiary = async (summaryId: string) => {
  const response = await axiosInstance.patch(`/api/summary/${summaryId}`);
  return response.data;
};

// 하루일기 삭제(오늘 제외)
export const deleteDiaries = async (summaryIds: string[]) => {
  const response = await axiosInstance.delete('/api/summary', {
    data: { summaryIds },
  });
  return response.data;
};
