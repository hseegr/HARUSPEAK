import { axiosInstance } from '@/apis/core';
import { GetLibraryParams, LibraryResponse } from '@/types/library';

// 하루 요약 일기 모아보기 - 최신순
export const getLibrary = async (params: GetLibraryParams) => {
  const response = await axiosInstance.get<LibraryResponse>('/api/summary', {
    params,
  });
  return response.data;
};

// 하루일기 삭제(오늘 제외) - 단일 summaryId 삭제
export const deleteDiary = async (summaryId: number) => {
  const response = await axiosInstance.delete(`/api/summary/${summaryId}`);
  return response.data;
};
