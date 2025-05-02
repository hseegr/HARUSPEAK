import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { deleteDiaries, editDiary, getLibrary } from '@/apis/myLibraryApi';

export interface Diary {
  summaryId: number;
  diaryDate: string;
  imageUrl: string;
  title: string;
  content: string;
  isImageGenerating: boolean;
  imageGenerateCount: number;
  contentGenerateCount: number;
  momentCount: number;
}

interface ResInfo {
  dataCount: number;
  nextCursor: string;
  hasMore: boolean;
}

interface LibraryResponse {
  data: Diary[];
  resInfo: ResInfo;
}

export const useGetLibrary = () =>
  useQuery<LibraryResponse>({
    queryKey: ['library'],
    queryFn: () => getLibrary(),
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

export const useDeleteDiaries = () => {
  const queryClient = useQueryClient();

  const { mutate } = useMutation({
    mutationFn: deleteDiaries,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['library'] });
    },
    onError: error => {
      console.log(error);
    },
  });
  return mutate;
};
