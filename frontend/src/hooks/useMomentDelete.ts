import { useMutation, useQueryClient } from '@tanstack/react-query';

import { deleteMoment } from '@/apis/todayApi';

export const useMomentDelete = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (createdAt: string) => deleteMoment(createdAt),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['todayDiary'] });
    },
  });
};
