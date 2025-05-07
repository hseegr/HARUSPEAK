import {
  useInfiniteQuery,
  useMutation,
  useQueryClient,
} from '@tanstack/react-query';

import { deleteDiaries, getLibrary } from '@/apis/libraryApi';
import { mockGetLibrary } from '@/mock/mockLibraryApi';

// 테스트를 위해 mock 데이터 사용 여부를 설정하는 상수
const useMockData = true;

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

export interface ResInfo {
  dataCount: number;
  nextCursor: string;
  hasMore: boolean;
}

export interface LibraryResponse {
  data: Diary[];
  resInfo: ResInfo;
}

export interface LibraryParams {
  limit?: number;
  before?: string;
  startDate?: string;
  endDate?: string;
}

// useInfiniteQuery를 사용하여 무한 스크롤 구현
export const useGetLibrary = ({
  limit = 30,
  startDate,
  endDate,
}: LibraryParams = {}) =>
  useInfiniteQuery<LibraryResponse, Error>({
    queryKey: ['library', limit, startDate, endDate],
    queryFn: ({ pageParam }) =>
      useMockData
        ? mockGetLibrary({
            limit,
            before: pageParam as string,
            startDate,
            endDate,
          })
        : getLibrary({
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
  });

export const useDeleteDiaries = () => {
  const queryClient = useQueryClient();

  const { mutateAsync } = useMutation({
    mutationFn: deleteDiaries,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['library'] });
    },
    onError: error => {
      console.log(error);
    },
  });
  return mutateAsync;
};
