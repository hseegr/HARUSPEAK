import { Plus } from 'lucide-react';

// import { useNavigate } from 'react-router-dom';

import myTodayOff from '@/assets/images/mytoday_off.png';
import todayOff from '@/assets/images/today_off.png';

// import myTodayOn from '@/assets/images/mytoday_on.png';
// import todayOn from '@/assets/images/today_on.png';

const NavigationBar = () => {
  return (
    <nav className='fixed bottom-0 left-1/2 flex h-[70px] w-full -translate-x-1/2 items-center justify-around rounded-t-2xl bg-white shadow-[4px_4px_16px_12px_rgba(0,0,0,0.04)]'>
      {/* 오늘의 기록 버튼 */}
      <button className='flex w-1/2 flex-col items-center justify-center text-xs'>
        <img className='w-6' src={todayOff} alt='' />
        <span className='text-haru-gray-4'>오늘의 기록</span>
      </button>

      {/* 플로팅 + 버튼 */}
      <button className='bg-haru-green absolute left-1/2 top-0 flex h-16 w-16 -translate-x-1/2 -translate-y-1/4 items-center justify-center rounded-full text-white'>
        <Plus />
      </button>

      {/* 내 서재 버튼 */}
      <button className='flex w-1/2 flex-col items-center justify-center text-xs'>
        <img className='w-6' src={myTodayOff} alt='' />
        <span className='text-haru-gray-4'>내 서재</span>
      </button>
    </nav>
  );
};

export default NavigationBar;
