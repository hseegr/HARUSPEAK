import { Home, LogOut, Plus } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

import { userLogout } from '@/apis/accountApi';
import myTodayOff from '@/assets/images/mytoday_off.png';
import todayOff from '@/assets/images/today_off.png';

const NavigationBar = () => {
  const navigate = useNavigate();

  return (
    <nav className='fixed bottom-0 flex h-[70px] w-full max-w-96 items-center justify-around rounded-t-2xl bg-white shadow-[4px_4px_16px_12px_rgba(0,0,0,0.04)]'>
      {/* Home */}
      <button
        className='flex w-1/2 flex-col items-center justify-center text-xs text-haru-gray-4'
        onClick={() => navigate('/')}
      >
        <Home />
        <span className='mt-1'>홈</span>
      </button>
      {/* 오늘의 기록 버튼 */}
      <button
        className='flex w-1/2 flex-col items-center justify-center pr-10 text-xs text-haru-gray-4'
        onClick={() => navigate('/today')}
      >
        <img className='w-6' src={todayOff} alt='' />
        <span className='mt-1'>오늘의 기록</span>
      </button>

      {/* 플로팅 + 버튼 */}
      <button
        className='absolute left-1/2 top-0 flex h-16 w-16 -translate-x-1/2 -translate-y-1/4 items-center justify-center rounded-full bg-haru-green text-white'
        onClick={() => navigate('/todaywrite')}
      >
        <Plus />
      </button>

      {/* 내 서재 버튼 */}
      <button
        className='flex w-1/2 flex-col items-center justify-center pl-10 text-xs text-haru-gray-4'
        onClick={() => navigate('/library')}
      >
        <img className='w-6' src={myTodayOff} alt='' />
        <span className='mt-1'>내 서재</span>
      </button>

      {/* 로그아웃 */}
      <button
        className='flex w-1/2 flex-col items-center justify-center text-xs text-haru-gray-4'
        onClick={() => userLogout()}
      >
        <LogOut />
        <span className='mt-1'>로그아웃</span>
      </button>
    </nav>
  );
};

export default NavigationBar;
