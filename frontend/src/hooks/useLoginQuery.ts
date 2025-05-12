import { useQuery } from '@tanstack/react-query';

import { userInfo } from '@/apis/accountApi';
import useAuthStore from '@/store/userStore';

// 사용자 정보 조회하는 쿼리
export const useUserInfoQuery = (options?: { enabled?: boolean }) => {
  const isLoggedIn = useAuthStore(state => state.user !== null);
  const isInitialCheck = options?.enabled === true;

  return useQuery({
    queryKey: ['userInfo'],
    queryFn: userInfo,
    retry: false,
    enabled: isInitialCheck ? true : isLoggedIn, // 초기 체크일 때는 무조건 활성화
    ...options,
  });
};
