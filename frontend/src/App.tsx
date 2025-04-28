import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
// ReactQueryDevtools : 쿼리 상태를 시각적으로 확인할 수 있는 개발자 도구
import { RouterProvider } from 'react-router-dom';

import { router } from '@/router';

import './App.css';

// QueryClient 인스턴스 생성
// 이 인스턴스는 캐시, 쿼리 동기화, 리페치 등의 설정을 관리한다.
const queryClient = new QueryClient();

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
      <ReactQueryDevtools />
    </QueryClientProvider>
  );
};

export default App;
