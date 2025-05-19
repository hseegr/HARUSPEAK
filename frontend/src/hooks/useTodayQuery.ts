import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'react-toastify';

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
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['todayDiary'] });
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

  return useMutation({
    mutationFn: recommendTag,
    onSuccess: (response, variables) => {
      toast.success('태그가 성공적으로 생성되었습니다.');

      // 쿼리 클라이언트를 통해 태그 업데이트
      if (!isEditPage) {
        queryClient.setQueryData(['todayDiary'], (oldData: any) => {
          if (!oldData || !oldData.data) return oldData;
          return {
            ...oldData,
            data: oldData.data.map((moment: any) =>
              moment.momentTime === variables.createdAt
                ? { ...moment, tags: response.recommendTags }
                : moment,
            ),
          };
        });
      }
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
