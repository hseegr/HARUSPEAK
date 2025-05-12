import { Navigate } from 'react-router-dom';

import { googleLogin } from '@/apis/accountApi';
import googleLogo from '@/assets/images/googlelogin.png';
import { useUserInfoQuery } from '@/hooks/useLoginQuery';
import useAuthStore from '@/store/userStore';

const LoginPage = () => {
  const { user } = useAuthStore();
  const { data: userInfo, isLoading } = useUserInfoQuery();

  // 로딩 중이면 로딩 상태 표시
  if (isLoading) {
    return <div>Loading...</div>;
  }

  // 로그인된 사용자면 홈으로 리다이렉트
  if (user || userInfo) {
    return <Navigate to='/' replace />;
  }

  const handleGoogleLogin = () => {
    googleLogin();
  };

  return (
    // 기존 baselayout 스타일 유지하기 위해 동일하게 구현 (현재 로그인 페이지는 baselayout 사용하지 않음)
    <div className='flex flex-col items-center bg-gray-100'>
      <div className='min-h-lvh w-full max-w-96 bg-white'>
        <div className='flex min-h-[100vh] w-full flex-col items-center justify-center gap-20'>
          <div className='flex flex-col items-center gap-2'>
            <div className='text-2xl font-bold'>하루스픽</div>
            <div className='text-sm font-medium'>
              간편하게 로그인하고 Haru's Peak 시작하기
            </div>
          </div>
          <div className='flex w-1/2 justify-center'>
            <button
              onClick={handleGoogleLogin}
              className='flex items-center justify-center'
            >
              <img src={googleLogo} alt='googleLogo' className='w-64' />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
