import { Navigate, Outlet, useLocation } from 'react-router-dom';

import useAuthStore from '@/store/userStore';

import LoadingSpinner from './LoadingSpinner';

/**
 * 보호된 라우트를 처리하는 컴포넌트
 * 인증이 필요한 페이지에 대한 접근을 제어
 * - 인증되지 않은 사용자 : 로그인으로 리다이렉트 + 로그인 후 가려던 페이지로 돌아가기 (히스토리 날림)
 * - 인증된 사용자 : 요청한 페이지로 접근 허용
 */
const ProtectedRoute = () => {
  const { user } = useAuthStore();
  const location = useLocation();

  // 초기 상태일때 (user 정보를 아직 가져오지 못한 상태, userInfoQuery 데이터 가져오는 중)
  if (user === undefined) {
    return <LoadingSpinner />;
  }

  // 로그인되지 않은 상태 (인증되지 않은 사용자)
  if (user === null) {
    return <Navigate to='/login' state={{ from: location.pathname }} replace />;
  }

  // user가 있는 상태 (인증된 사용자)
  return <Outlet />;
};

export default ProtectedRoute;
