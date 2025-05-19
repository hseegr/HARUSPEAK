import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'react-toastify';

import {
  deleteMoment,
  getToday,
  recommendTag,
  updateMoment,
} from '@/apis/todayApi';
import {
  TagRequest,
  TagResponse,
  TodayDiaryResponse,
  UpdateMomentRequest,
} from '@/types/today';

export const useToday = () => {
  return useQuery<TodayDiaryResponse>({
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
    onSuccess: (_, variables) => {
      // 즉시 UI 업데이트만 수행
      queryClient.setQueryData<TodayDiaryResponse>(['todayDiary'], oldData => {
        if (!oldData || !oldData.data) return oldData;
        return {
          ...oldData,
          data: oldData.data.map(moment =>
            moment.createdAt === variables.createdAt
              ? { ...moment, ...variables.data }
              : moment,
          ),
        };
      });

      toast.success('순간 기록이 수정되었습니다.');
    },
    onError: error => {
      toast.error(
        error instanceof Error
          ? error.message
          : '순간 기록 수정 중 오류가 발생했습니다.',
      );
    },
  });
};

export const useMomentDelete = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (createdAt?: string) => {
      if (!createdAt) {
        throw new Error('createdAt이 없습니다.');
      }
      return deleteMoment(createdAt);
    },
    onSuccess: (_, createdAt) => {
      // 즉시 UI 업데이트만 수행
      queryClient.setQueryData<TodayDiaryResponse>(['todayDiary'], oldData => {
        if (!oldData || !oldData.data) return oldData;
        return {
          ...oldData,
          data: oldData.data.filter(moment => moment.createdAt !== createdAt),
        };
      });

      toast.success('순간 기록이 삭제되었습니다.');
    },
    onError: error => {
      toast.error(
        error instanceof Error
          ? error.message
          : '순간 기록 삭제 중 오류가 발생했습니다.',
      );
    },
  });
};

export const useMomentTagRecommend = (isEditPage: boolean = false) => {
  const queryClient = useQueryClient();

  return useMutation<TagResponse, Error, TagRequest>({
    mutationFn: recommendTag,
    onSuccess: (response, variables) => {
      // 즉시 UI 업데이트만 수행
      if (!isEditPage) {
        queryClient.setQueryData<TodayDiaryResponse>(
          ['todayDiary'],
          oldData => {
            if (!oldData || !oldData.data) return oldData;
            return {
              ...oldData,
              data: oldData.data.map(moment =>
                moment.createdAt === variables.createdAt
                  ? { ...moment, tags: response.recommendTags }
                  : moment,
              ),
            };
          },
        );
      }

      toast.success('태그가 성공적으로 생성되었습니다.');
    },
    onError: error => {
      toast.error(
        error instanceof Error
          ? error.message
          : '태그 생성 중 오류가 발생했습니다.',
      );
    },
  });
};
