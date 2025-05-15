import { PropsWithChildren, useEffect } from 'react';

import LoadingSpinner from '@/components/LoadingSpinner';
import { useUserInfoQuery } from '@/hooks/useLoginQuery';
import useAuthStore from '@/store/userStore';

const LoginProvider = ({ children }: PropsWithChildren) => {
  const { setUser } = useAuthStore();
  const { data, isLoading } = useUserInfoQuery();

  useEffect(() => {
    if (isLoading) {
      return;
    }
    setUser(data || null);
  }, [data, isLoading, setUser]);

  // 로딩 중일 때는 로딩 UI 표시
  if (isLoading) {
    return <LoadingSpinner />;
  }

  return <>{children}</>;
};

export default LoginProvider;
