import { useEffect, useState } from 'react';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
// ReactQueryDevtools : 쿼리 상태를 시각적으로 확인할 수 있는 개발자 도구
import { RouterProvider } from 'react-router-dom';

import { userInfo } from '@/apis/accountApi';
import { router } from '@/router';
import useAuthStore from '@/store/userStore';

// QueryClient 인스턴스 생성
// 이 인스턴스는 캐시, 쿼리 동기화, 리페치 등의 설정을 관리한다.
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

const AppContent = () => {
  const { setUser, clearUser } = useAuthStore();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const initializeAuth = async () => {
      try {
        const userData = await userInfo();
        setUser(userData);
      } catch {
        clearUser();
      } finally {
        setLoading(false);
      }
    };
    initializeAuth();
  }, [setUser, clearUser]);

  if (loading) {
    return <div>로딩 중...</div>; // 필요시 스플래시 컴포넌트로 교체 가능
  }

  return <RouterProvider router={router} />;
};

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <AppContent />
      <ReactQueryDevtools />
    </QueryClientProvider>
  );
};

export default App;
