import { useQuery } from '@tanstack/react-query';

import { userInfo } from '@/apis/accountApi';

// 사용자 정보 조회하는 쿼리
export const useUserInfoQuery = () => {
  return useQuery({
    queryKey: ['userInfo'],
    queryFn: userInfo,
  });
};
