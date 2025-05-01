import { HomeStatisticsResponse } from '@/types/home';

export const getHomeStatistics = (): Promise<HomeStatisticsResponse> => {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve({
        todayMomentCount: 10,
        totalMomentCount: 42,
        totalDayCount: 15,
      });
    }, 500);
  });
};
