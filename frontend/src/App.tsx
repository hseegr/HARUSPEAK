import { useEffect } from 'react';

import { RouterProvider } from 'react-router-dom';

import { useUserInfoQuery } from '@/hooks/useLoginQuery';
import LoginQueryProvider from '@/providers/LoginQueryProvider';
import { router } from '@/router';
import useAuthStore from '@/store/userStore';

const AppContent = () => {
  const { setUser, clearUser } = useAuthStore();
  const { data, isLoading, isError } = useUserInfoQuery();

  useEffect(() => {
    if (data) {
      setUser(data);
    } else if (isError) {
      clearUser();
    }
  }, [data, isError, setUser, clearUser]);

  if (isLoading) {
    return <div>로딩 중...</div>; // 필요시 스플래시 컴포넌트로 교체 가능
  }

  return <RouterProvider router={router} />;
};

const App = () => {
  return (
    <LoginQueryProvider>
      <AppContent />
    </LoginQueryProvider>
  );
};

export default App;
