import { useState } from 'react';

import EmojiSelector from '@/components/EmojiSelector';
import TodayMoments from '@/components/TodayMoments';
import { useHomeStatisticsQuery } from '@/hooks/useHomeStatisticsQuery';
import useAuthStore from '@/store/userStore';

const Home = () => {
  const { user } = useAuthStore();
  const { data: statistics } = useHomeStatisticsQuery();
  const [isEmojiSelectorOpen, setIsEmojiSelectorOpen] = useState(false);

  return (
    <div className='flex min-h-[80vh] w-full flex-col'>
      <div className='flex h-full flex-1 flex-col justify-between'>
        {/* 인사말 섹션 */}
        <section className='text-center text-lg'>
          <p>
            안녕하세요
            {user?.name && (
              <span className='ml-1 font-bold text-haru-green'>
                {user.name}
              </span>
            )}
            님
          </p>
          <p>오늘 하루 잘 보내고 계신가요?</p>
        </section>

        {statistics && (
          <>
            {/* 오늘의 순간 섹션 */}
            <section className='w-full items-center rounded-lg'>
              {statistics.todayCount > 0 ? (
                <>
                  <TodayMoments momentCount={statistics.todayCount} />
                  <p className='w-full text-center font-leeseyoon text-lg text-haru-gray-5'>
                    <span className='mr-1'>오늘</span>
                    <span className='font-bold text-haru-green'>
                      {statistics.todayCount}
                    </span>
                    <span>개의 순간을 기록했어요</span>
                  </p>
                  <div className='mt-2 flex justify-center'>
                    <button
                      onClick={() => setIsEmojiSelectorOpen(true)}
                      className='text-sm font-bold text-haru-light-green hover:text-haru-green'
                    >
                      이모지 선택
                    </button>
                  </div>
                </>
              ) : (
                <div className='flex h-80 flex-col items-center justify-center'>
                  <span className='animate-bounce-and-rotate text-4xl'>😊</span>
                  <p className='mt-2 text-center text-xl font-bold text-haru-green'>
                    하루 기록이 없어요
                  </p>
                  <p className='mt-2 text-center text-sm text-haru-gray-5'>
                    소중한 순간을 남겨보세요
                  </p>
                </div>
              )}
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
                  {statistics.totalDiaryCount}일
                </p>
              </div>
            </section>
          </>
        )}
      </div>

      <EmojiSelector
        isOpen={isEmojiSelectorOpen}
        onClose={() => setIsEmojiSelectorOpen(false)}
      />
    </div>
  );
};

export default Home;
