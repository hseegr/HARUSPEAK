import { useQuery } from '@tanstack/react-query';

import { fetchTodayDiary } from '@/mock/mockTodayApi';

// 나중에 실제 api로 갈아끼움

export const useToday = () => {
  return useQuery({
    queryKey: ['todayDiary'],
    queryFn: fetchTodayDiary,
  });
};
