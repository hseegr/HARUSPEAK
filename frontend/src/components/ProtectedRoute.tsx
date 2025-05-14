import { Navigate, Outlet } from 'react-router-dom';

import useAuthStore from '@/store/userStore';

const ProtectedRoute = () => {
  const { user } = useAuthStore();

  // user가 null이면 로그인되지 않은 상태
  if (user === null) {
    return <Navigate to='/login' replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
