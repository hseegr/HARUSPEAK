import { PropsWithChildren, useEffect } from 'react';

import { useUserInfoQuery } from '@/hooks/useLoginQuery';
import useAuthStore from '@/store/userStore';

const LoginProvider = ({ children }: PropsWithChildren) => {
  const { setUser } = useAuthStore();
  const isLoginPage = window.location.pathname === '/login';

  const { data, isLoading } = useUserInfoQuery();

  console.log(
    'LoginProvider - isLoginPage:',
    isLoginPage,
    'isLoading:',
    isLoading,
    'data:',
    data,
  );

  useEffect(() => {
    if (data) {
      console.log('LoginProvider - 사용자 정보 설정:', data);
      setUser(data);
    }
  }, [data, setUser]);

  return <>{children}</>;
};

export default LoginProvider;
