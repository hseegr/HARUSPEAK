import { Navigate, Outlet } from 'react-router-dom';

import useAuthStore from '@/store/userStore';

const ProtectedRoute = () => {
  const { isAuthenticated } = useAuthStore();

  // 로그인 안한 사용자가 다른 페이지로 이동
  if (!isAuthenticated) {
    return <Navigate to='/login' replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
