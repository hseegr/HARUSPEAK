import { TagRequest, UpdateMomentRequest } from '@/types/today';

import { axiosInstance } from './core';

// GET : 오늘의 일기 불러오기 (하루의 순간들 모아보기)
export const getToday = async () => {
  const response = await axiosInstance.get('/api/today');
  return response.data;
};

// POST : 순간 태그 생성
export const recommendTag = async (data: TagRequest) => {
  const response = await axiosInstance.post('/api/today/tags', data, {
    timeout: 5000,
  });
  return response.data;
};

// PATCH : 오늘의 순간 일기 수정
export const updateMoment = async (
  createdAt: string,
  data: UpdateMomentRequest,
) => {
  const response = await axiosInstance.patch(
    `/api/today/time/${createdAt}`,
    data,
  );
  return response.data;
};

// DELETE : 오늘의 순간 일기 삭제
export const deleteMoment = async (time: string) => {
  const response = await axiosInstance.delete(`/api/today/time/${time}`);
  return response.data;
};
