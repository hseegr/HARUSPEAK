import { useState } from 'react';

import EmojiSelector from '@/components/EmojiSelector';
import TodayMoments from '@/components/TodayMoments';
import { useHomeStatisticsQuery } from '@/hooks/useHomeStatisticsQuery';
import useAuthStore from '@/store/userStore';
import { defaultEmojis } from '@/types/common';

const Home = () => {
  const { user } = useAuthStore();
  const { data: statistics } = useHomeStatisticsQuery();
  const [isEmojiSelectorOpen, setIsEmojiSelectorOpen] = useState(false);
  const [selectedEmojis, setSelectedEmojis] = useState<string[]>(() => {
    const savedEmojis = localStorage.getItem('selectedEmojis');
    return savedEmojis ? JSON.parse(savedEmojis) : defaultEmojis;
  });

  const handleEmojiSelect = (emojis: string[]) => {
    setSelectedEmojis(emojis);
    localStorage.setItem('selectedEmojis', JSON.stringify(emojis));
  };

  return (
    <div className='flex min-h-[80vh] w-full flex-col'>
      <div className='flex h-full flex-1 flex-col justify-between gap-3'>
        {/* 인사말 섹션 */}
        <section className='pt-2 text-center'>
          <p>
            안녕하세요
            {user?.name && (
              <span className='ml-1 font-bold'>{user.name}님</span>
            )}
          </p>
          <p>오늘 하루 잘 보내고 계신가요?</p>
        </section>

        {statistics && (
          <>
            {/* 오늘의 순간 섹션 */}
            <section className='w-full flex-1 items-center rounded-lg'>
              <TodayMoments momentCount={statistics.todayCount} />
              <p className='w-full text-center font-leeseyoon text-lg text-haru-gray-5'>
                <span className='mr-1'>오늘</span>
                <span className='font-bold text-haru-green'>
                  {statistics.todayCount}
                </span>
                <span>개의 순간을 기록했어요</span>
              </p>
              <div className='mb-2 flex justify-center'>
                <button
                  onClick={() => setIsEmojiSelectorOpen(true)}
                  className='text-sm font-bold text-haru-light-green hover:text-haru-green'
                >
                  이모지 선택
                </button>
              </div>
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
        onSelect={handleEmojiSelect}
        selectedEmojis={selectedEmojis}
      />
    </div>
  );
};

export default Home;
