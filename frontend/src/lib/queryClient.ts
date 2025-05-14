import { QueryCache, QueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { toast } from 'react-toastify';

export const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError: error => {
      if (error instanceof AxiosError) {
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
