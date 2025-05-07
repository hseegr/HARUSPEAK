import { useEffect, useState } from 'react';

import { getHomeStatistics } from '@/apis/homeApi';
import { HomeStatisticsResponse } from '@/types/home';

interface UseHomeStatisticsReturn {
  statistics: HomeStatisticsResponse | null;
  isLoading: boolean;
  error: string | null;
  refetch: () => Promise<void>;
  finishLoading: () => void;
}

export const useHomeStatistics = (): UseHomeStatisticsReturn => {
  const [statistics, setStatistics] = useState<HomeStatisticsResponse | null>(
    null,
  );
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchStatistics = async () => {
    try {
      setIsLoading(true);
      const data = await getHomeStatistics();
      setStatistics(data);
      setError(null);
    } catch (err) {
      setError('데이터를 불러올 수 없습니다');
      console.error('Failed to fetch statistics:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const finishLoading = () => {
    setIsLoading(false);
  };

  useEffect(() => {
    fetchStatistics();
  }, []);

  return {
    statistics,
    isLoading,
    error,
    refetch: fetchStatistics,
    finishLoading,
  };
};
