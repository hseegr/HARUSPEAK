import { useQuery } from '@tanstack/react-query';

import { getHomeStatistics } from '@/apis/homeApi';
import { HomeStatisticsResponse } from '@/types/home';

export const useHomeStatisticsQuery = () => {
  return useQuery<HomeStatisticsResponse>({
    queryKey: ['homeStatistics'],
    queryFn: getHomeStatistics,
  });
};
