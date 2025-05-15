import { useNavigate } from 'react-router-dom';

import TodayMoments from '@/components/TodayMoments';

const NotFound = () => {
  const navigate = useNavigate();

  return (
    <div className='flex h-[80vh] w-full flex-col items-center justify-center'>
      <div className='relative h-[350px] w-full'>
        <TodayMoments momentCount={8} />
      </div>
      <div className='absolute animate-fade-in text-9xl font-bold text-haru-light-green opacity-0'>
        404
      </div>
      <button
        onClick={() => navigate('/')}
        className='mt-8 rounded-lg bg-haru-light-green px-6 py-2 text-white hover:bg-haru-green'
      >
        홈으로 돌아가기
      </button>
    </div>
  );
};

export default NotFound;
