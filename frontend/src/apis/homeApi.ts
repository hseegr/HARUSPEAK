import { axiosInstance } from './core';

// 일기 스탯 정보 조회 (Home 화면에서 사용)
export const getHomeStatistics = async () => {
  const response = await axiosInstance.get('/api/main');
  return response.data;
};
