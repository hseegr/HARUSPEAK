import { Home, LogOut, Plus } from 'lucide-react';
import { useLocation, useNavigate } from 'react-router-dom';

import LibraryIcon from '@/assets/icons/LibraryIcon';
import TodayIcon from '@/assets/icons/TodayIcon';
import { useLogoutQuery } from '@/hooks/useLogoutQuery';

const baseButtonClasses =
  'flex w-1/2 flex-col items-center justify-center text-xs transition-colors duration-200';

const NavigationBar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { mutate: handleLogout } = useLogoutQuery();

  const getActiveColor = (path: string) =>
    location.pathname === path
      ? 'text-haru-green hover:text-haru-green'
      : 'text-haru-gray-4 hover:text-haru-gray-5';

  return (
    <nav className='fixed bottom-0 flex h-[70px] w-full max-w-96 items-center justify-around rounded-t-2xl bg-white shadow-[4px_4px_16px_12px_rgba(0,0,0,0.04)]'>
      {/* Home */}
      <button
        className={`${baseButtonClasses} ${getActiveColor('/')}`}
        onClick={() => navigate('/')}
        aria-label='Home으로 이동'
      >
        <Home />
        <span className='mt-1 font-bold'>홈</span>
      </button>
      {/* 오늘의 기록 버튼 */}
      <button
        className={`${baseButtonClasses} pr-10 ${getActiveColor('/today')}`}
        onClick={() => navigate('/today')}
        aria-label='오늘의 기록 보기'
      >
        <TodayIcon />
        <span className='mt-1 font-bold'>오늘의 기록</span>
      </button>

      {/* 플로팅 + 버튼 */}
      <button
        className='absolute left-1/2 top-0 flex h-16 w-16 -translate-x-1/2 -translate-y-1/4 items-center justify-center rounded-full bg-haru-green text-white transition-colors duration-200 hover:bg-haru-light-green'
        onClick={() => navigate('/todaywrite')}
        aria-label='오늘의 기록 추가'
      >
        <Plus />
      </button>

      {/* 내 서재 버튼 */}
      <button
        className={`${baseButtonClasses} pl-10 ${getActiveColor('/library')}`}
        onClick={() => navigate('/library')}
        aria-label='내 서재 가기'
      >
        <LibraryIcon />
        <span className='mt-1 font-bold'>내 서재</span>
      </button>

      {/* 로그아웃 */}
      <button
        className={`${baseButtonClasses} text-haru-gray-4 hover:text-haru-gray-5`}
        onClick={() => handleLogout()}
        aria-label='로그아웃'
      >
        <LogOut />
        <span className='mt-1 font-bold'>로그아웃</span>
      </button>
    </nav>
  );
};

export default NavigationBar;
