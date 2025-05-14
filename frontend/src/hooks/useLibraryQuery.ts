import {
  useInfiniteQuery,
  useMutation,
  useQueryClient,
} from '@tanstack/react-query';
import { toast } from 'react-toastify';

import { deleteDiary, getLibrary } from '@/apis/libraryApi';
import { LibraryParams, LibraryResponse } from '@/types/library';

export const useGetLibrary = ({
  limit = 30,
  startDate,
  endDate,
}: LibraryParams = {}) =>
  useInfiniteQuery<LibraryResponse, Error>({
    queryKey: ['library', limit, startDate, endDate],
    queryFn: ({ pageParam }) =>
      getLibrary({
        limit,
        before: pageParam as string,
        startDate,
        endDate,
      }),
    initialPageParam: undefined,
    getNextPageParam: lastPage => {
      if (!lastPage?.resInfo?.hasMore) return undefined;
      return lastPage.resInfo.nextCursor;
    },
    structuralSharing: true,
  });

export const useDeleteDiary = () => {
  const queryClient = useQueryClient();

  const { mutateAsync, isPending } = useMutation({
    mutationFn: deleteDiary,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['library'] });
      toast.success('일기가 성공적으로 삭제되었습니다.');
    },
    onError: error => {
      toast.error(error.message);
    },
  });
  return { mutateAsync, isPending };
};
