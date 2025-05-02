import { useEffect, useState } from 'react';

import SplashScreen from '@/components/SplashScreen';
import TodayMoments from '@/components/TodayMoments';
import { getHomeStatistics } from '@/mock/mockHomeApi';
import { HomeStatisticsResponse } from '@/types/home';

const Home = () => {
  const [statistics, setStatistics] = useState<HomeStatisticsResponse | null>(
    null,
  );
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchStatistics = async () => {
      const data = await getHomeStatistics();
      setStatistics(data);
    };
    fetchStatistics();
  }, []);

  const handleSplashFinish = () => {
    setIsLoading(false);
  };

  if (isLoading) {
    return <SplashScreen onFinish={handleSplashFinish} />;
  }

  return (
    <div className='flex min-h-80 w-full flex-col'>
      <div className='flex flex-1 flex-col justify-between gap-8 p-4'>
        {/* 인사말 섹션 */}
        <section className='pt-2 text-center'>
          <p>안녕하세요 [사용자]님</p>
          <p>오늘 하루 잘 보내고 계신가요?</p>
        </section>

        {statistics && (
          <>
            {/* 오늘의 순간 섹션 */}
            <section className='min-h-96 w-full flex-1 items-center rounded-lg'>
              <TodayMoments momentCount={statistics.todayMomentCount} />
              <p className='w-full text-center text-haru-gray-5'>
                오늘 {statistics.todayMomentCount}개의 순간을 기록했어요
              </p>
            </section>

            {/* 전체 통계 섹션 */}
            <section className='w-full rounded-lg bg-haru-beige p-4'>
              <div className='mb-2 flex justify-between'>
                <p className='text-haru-gray-5'>지금까지 기록한 순간들 : </p>
                <p className='font-bold text-haru-green'>
                  {statistics.totalMomentCount}개
                </p>
              </div>
              <div className='flex justify-between'>
                <p className='text-haru-gray-5'>지금까지 기록한 하루들 : </p>
                <p className='font-bold text-haru-green'>
                  {statistics.totalDayCount}일
                </p>
              </div>
            </section>
          </>
        )}
      </div>
    </div>
  );
};

export default Home;
