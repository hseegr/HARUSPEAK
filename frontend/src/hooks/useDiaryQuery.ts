import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { toast } from 'react-toastify';

import {
  editDiary,
  getDiary,
  regenerateContent,
  regenerateImage,
} from '@/apis/diaryApi';
import { DiaryResponse } from '@/types/diary';
import { ErrorResponse } from '@/types/error';

export const useGetDiary = (summaryId: string) =>
  useQuery<DiaryResponse>({
    queryKey: ['diary', summaryId],
    queryFn: () => getDiary(summaryId),
    enabled: !!summaryId, // summaryId가 없으면 쿼리 실행 안함
    // 조건부 폴링: 이미지 생성 중일 때만 8초마다 재요청
    refetchInterval: query => {
      const isGenerating = query.state.data?.summary?.isImageGenerating;
      return isGenerating ? 8000 : false;
    },
    // 백그라운드에서도 폴링을 유지
    refetchIntervalInBackground: true,
  });

export const useRegenerateImage = () => {
  const queryClient = useQueryClient();

  const { mutate, isPending } = useMutation({
    mutationFn: regenerateImage,
    onSuccess: (_, summaryId) => {
      queryClient.invalidateQueries({ queryKey: ['diary', summaryId] });
      queryClient.invalidateQueries({ queryKey: ['library'] });
      toast.info('이미지를 재생성하고 있습니다. 잠시만 기다려주세요.');
    },
    onError: (error: AxiosError<ErrorResponse>) => {
      toast.error(
        error.response?.data?.message ||
          '이미지 재생성 중 오류가 발생했습니다.',
      );
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
    onError: (error: AxiosError<ErrorResponse>) => {
      toast.error(
        error.response?.data?.message || '요약 재생성 중 오류가 발생했습니다.',
      );
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
