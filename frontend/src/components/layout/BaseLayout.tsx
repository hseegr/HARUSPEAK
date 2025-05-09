import { useEffect } from 'react';

import { Outlet, useMatches } from 'react-router-dom';

import { useUserInfoQuery } from '@/hooks/useLoginQuery';
import useAuthStore from '@/store/userStore';

import Header from './Header';
import NavigationBar from './NavigationBar';

const BaseLayout = () => {
  const matches = useMatches();
  const { setUser, clearUser } = useAuthStore();
  const { data, isLoading, isError } = useUserInfoQuery();

  useEffect(() => {
    if (data) {
      setUser(data);
    } else if (isError) {
      clearUser();
    }
  }, [data, isError, setUser, clearUser]);

  const currentTitle = matches
    .map(match => (match.handle as { title?: string })?.title)
    .filter(Boolean)
    .slice(-1)[0];

  const isHome = currentTitle === undefined;

  if (isLoading) {
    return <div>로딩 중...</div>; // 필요시 스플래시 컴포넌트로 교체 가능
  }

  return (
    <div className='flex flex-col items-center bg-gray-100'>
      <div className='min-h-lvh w-full max-w-96 bg-white'>
        {currentTitle && <Header title={currentTitle} />}
        <main
          className={`flex w-full overflow-y-auto px-4 pb-[80px] pt-[56px] ${isHome ? 'bg-haru-yellow' : 'bg-white'}`}
        >
          <Outlet />
        </main>
        <NavigationBar />
      </div>
    </div>
  );
};

export default BaseLayout;
