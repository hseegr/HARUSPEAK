import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import {
  editDiary,
  getDiary,
  getImage,
  regenerateContent,
  regenerateImage,
} from '@/apis/diaryApi';
import { DiaryResponse } from '@/types/diary';

export const useGetDiary = (summaryId: string) =>
  useQuery<DiaryResponse>({
    queryKey: ['diary', summaryId],
    queryFn: () => getDiary(summaryId),
  });

export const useRegenerateContent = () => {
  const queryClient = useQueryClient();

  const { mutate } = useMutation({
    mutationFn: regenerateContent,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['diary'] });
    },
    onError: error => {
      console.log(error);
    },
  });

  return mutate;
};

export const useRegenerateImage = () => {
  const queryClient = useQueryClient();

  const { mutate } = useMutation({
    mutationFn: regenerateImage,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['diary'] });
    },
    onError: error => {
      console.log(error);
    },
  });
  return mutate;
};

export const useGetImage = (summaryId: string) =>
  useQuery({
    queryKey: ['image', summaryId],
    queryFn: () => getImage(summaryId),
  });

export const useEditDiary = () => {
  const queryClient = useQueryClient();

  const { mutate } = useMutation({
    mutationFn: editDiary,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['library'] });
    },
    onError: error => {
      console.log(error);
    },
  });
  return mutate;
};
