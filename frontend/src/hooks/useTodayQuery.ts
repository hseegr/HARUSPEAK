import { useQuery } from '@tanstack/react-query';

// 나중에 실제 api로 갈아끼움
import { fetchTodayDiary } from '@/mock/mockTodayApi';

export const useToday = () => {
  return useQuery({
    queryKey: ['todayDiary'],
    queryFn: fetchTodayDiary,
  });
};
