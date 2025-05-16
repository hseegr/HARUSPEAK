import { QueryCache, QueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { toast } from 'react-toastify';

// TODO : queryCache 및 useQuery에서 에러처리 학습해서 중앙화 -> 불필요한 옵션이면 파일 삭제할 것
export const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError: error => {
      // useQuery에서 에러처리 통일 (401일 때는 별도 toast 안날림, userInfo 제외 로그인 전제 하에 동작하기에)
      if (error instanceof AxiosError && error.response?.status !== 401) {
        toast.error(
          error.response?.data?.message ?? '데이터를 불러오지 못했습니다.',
        );
      }
    },
  }),
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});
