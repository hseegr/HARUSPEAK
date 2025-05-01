import { useEffect, useState } from 'react';

import TodayMoments from '@/components/TodayMoments';
import { getHomeStatistics } from '@/mock/mockHomeApi';
import { HomeStatisticsResponse } from '@/types/home';

const Home = () => {
  const [statistics, setStatistics] = useState<HomeStatisticsResponse | null>(
    null,
  );

  useEffect(() => {
    const fetchStatistics = async () => {
      const data = await getHomeStatistics();
      setStatistics(data);
    };
    fetchStatistics();
  }, []);

  return (
    <div className='flex flex-col w-full'>
      <div className='flex flex-col justify-between flex-1 gap-8 p-4'>
        {/* 인사말 섹션 */}
        <section className='pt-2 text-center'>
          <p className='text-xl text-gray-600'>안녕하세요.</p>
          <p>오늘 하루를 잘 보내고 계신가요?</p>
        </section>

        {statistics && (
          <>
            {/* 오늘의 순간 섹션 */}
            <section className='items-center flex-1 w-full bg-white rounded-lg min-h-96'>
              <TodayMoments momentCount={statistics.todayMomentCount} />
              <p className='w-full text-center text-gray-600'>
                오늘 {statistics.todayMomentCount}개의 순간을 기록했어요
              </p>
            </section>

            {/* 전체 통계 섹션 */}
            <section className='w-full p-4 rounded-lg bg-haru-beige'>
              <div className='flex justify-between mb-2'>
                <p className='text-gray-600'>지금까지 기록한 순간들 : </p>
                <p className='font-bold text-green-500'>
                  {statistics.totalMomentCount}개
                </p>
              </div>
              <div className='flex justify-between'>
                <p className='text-gray-600'>지금까지 기록한 하루들 : </p>
                <p className='font-bold text-green-500'>
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
