import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'react-toastify';

import {
  editDiary,
  getDiary,
  regenerateContent,
  regenerateImage,
} from '@/apis/diaryApi';
import { DiaryResponse } from '@/types/diary';

export const useGetDiary = (summaryId: string) =>
  useQuery<DiaryResponse>({
    queryKey: ['diary', summaryId],
    queryFn: () => getDiary(summaryId),
    enabled: !!summaryId, // summaryId가 없으면 쿼리 실행 안함
  });

export const useRegenerateImage = () => {
  const queryClient = useQueryClient();

  const { mutate, isPending } = useMutation({
    mutationFn: regenerateImage,
    onSuccess: (_, summaryId) => {
      queryClient.invalidateQueries({ queryKey: ['diary', summaryId] });
    },
    onError: error => {
      toast.error(error.message || '이미지 재생성 중 오류가 발생했습니다.');
    },
  });
  return { mutate, isPending };
};

export const useRegenerateContent = () => {
  const queryClient = useQueryClient();

  const { mutate, isPending } = useMutation({
    mutationFn: regenerateContent,
    onSuccess: (_, summaryId) => {
      queryClient.invalidateQueries({ queryKey: ['diary', summaryId] });
      toast.success('요약 재생성이 완료되었습니다.');
    },
    onError: error => {
      toast.error(error.message || '요약 재생성 중 오류가 발생했습니다.');
    },
  });

  return { mutate, isPending };
};

// export const useGetImage = (summaryId: string) =>
//   useQuery({
//     queryKey: ['image', summaryId],
//     queryFn: () => getImage(summaryId),
//   });

export const useEditDiary = () => {
  const queryClient = useQueryClient();

  const { mutate, isPending } = useMutation({
    mutationFn: ({
      summaryId,
      title,
      content,
    }: {
      summaryId: string;
      title: string;
      content: string;
    }) => editDiary(summaryId, title, content),
    onSuccess: (_, variables) => {
      // 성공 시 해당 일기와 라이브러리 데이터 모두 무효화
      queryClient.invalidateQueries({
        queryKey: ['diary', variables.summaryId],
      });
      queryClient.invalidateQueries({ queryKey: ['library'] });
      toast.success('일기 수정이 완료되었습니다.');
    },
    onError: error => {
      toast.error(error.message || '일기 수정 중 오류가 발생했습니다.');
    },
  });
  return { mutate, isPending };
};
