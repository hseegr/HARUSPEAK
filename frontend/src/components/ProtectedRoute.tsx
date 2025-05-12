import { Navigate, Outlet } from 'react-router-dom';

import { useUserInfoQuery } from '@/hooks/useLoginQuery';

const ProtectedRoute = () => {
  const { isLoading, data } = useUserInfoQuery({ enabled: true });

  // 로딩 중이면 현재 페이지 유지
  if (isLoading) {
    return <Outlet />;
  }

  // 로딩이 끝났고 사용자 정보가 없을 때만 리다이렉트
  if (!data) {
    return <Navigate to='/login' replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
