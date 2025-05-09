import { useEffect } from 'react';

import { Navigate, Outlet } from 'react-router-dom';

import { useUserInfoQuery } from '@/hooks/useLoginQuery';
import useAuthStore from '@/store/userStore';

const ProtectedRoute = () => {
  const { isLoading, isError, data } = useUserInfoQuery();
  const { setUser, clearUser } = useAuthStore();

  useEffect(() => {
    if (data) setUser(data);
    else if (isError) clearUser();
  }, [data, isError, setUser, clearUser]);

  if (isLoading) {
    return <div>로딩 중...</div>;
  }

  if (isError || !data) {
    return <Navigate to='/login' replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
