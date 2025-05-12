import { PropsWithChildren, useEffect } from 'react';

import { useUserInfoQuery } from '@/hooks/useLoginQuery';
import useAuthStore from '@/store/userStore';

const LoginProvider = ({ children }: PropsWithChildren) => {
  const { setUser } = useAuthStore();

  const { data } = useUserInfoQuery();

  useEffect(() => {
    if (data) {
      setUser(data);
    }
  }, [data, setUser]);

  return <>{children}</>;
};

export default LoginProvider;
