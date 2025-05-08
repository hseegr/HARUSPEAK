import {
  useInfiniteQuery,
  useMutation,
  useQueryClient,
} from '@tanstack/react-query';

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
    // 불필요한 리렌더링 방지
    structuralSharing: true,
  });

export const useDeleteDiary = () => {
  const queryClient = useQueryClient();

  const { mutateAsync } = useMutation({
    mutationFn: deleteDiary,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['library'] });
    },
    onError: error => {
      console.log(error);
    },
  });
  return mutateAsync;
};
