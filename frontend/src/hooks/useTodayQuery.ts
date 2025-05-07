import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import {
  deleteMoment,
  getToday,
  recommendTag,
  updateMoment,
} from '@/apis/todayApi';
import { UpdateMomentRequest } from '@/types/today';

export const useToday = () => {
  return useQuery({
    queryKey: ['todayDiary'],
    queryFn: getToday,
  });
};

export const useMomentEdit = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      createdAt,
      data,
    }: {
      createdAt: string;
      data: UpdateMomentRequest;
    }) => updateMoment(createdAt, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['todayDiary'] });
    },
  });
};

export const useMomentDelete = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteMoment,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['todayDiary'] });
    },
  });
};

export const useMomentTagRecommend = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: recommendTag,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['todayDiary'] });
    },
  });
};
