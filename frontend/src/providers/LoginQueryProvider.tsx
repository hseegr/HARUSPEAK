import { PropsWithChildren, useEffect } from 'react';

import { useUserInfoQuery } from '@/hooks/useLoginQuery';
import useAuthStore from '@/store/userStore';

/**
 * 사용자 인증 상태를 관리하는 Provider 컴포넌트
 * useUserInfoQuery를 통해 사용자 정보를 가져와 전역 상태에 저장
 */
const LoginProvider = ({ children }: PropsWithChildren) => {
  const { setUser } = useAuthStore();
  const { data, isLoading } = useUserInfoQuery();

  useEffect(() => {
    // isLoading이 false일 때 (데이터 로딩 완료) 로그인 유지 or null 설정
    if (!isLoading) {
      setUser(data || null);
    }
  }, [data, setUser, isLoading]);

  return <>{children}</>;
};

export default LoginProvider;
