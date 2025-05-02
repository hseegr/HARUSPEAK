import { useMutation } from '@tanstack/react-query';

import { todayWriteSend } from '@/apis/todayWriteApi';
import { TodayWrite } from '@/types/todayWrite';

// 일기 작성 쿼리
export const useTodayWriteMutation = () => {
  return useMutation({
    mutationFn: (data: TodayWrite) => todayWriteSend(data),
  });
};
