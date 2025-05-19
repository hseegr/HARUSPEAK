import EmptyState from '@/components/EmptyState';
import MomentCard from '@/components/MomentCard';
import { useToday } from '@/hooks/useTodayQuery';
import { MomentContent } from '@/types/common';

const TodayPage = () => {
  const { data, isLoading } = useToday();

  if (isLoading) return <div>로딩중...</div>;
  if (!data?.data || data.dataCount === 0)
    return (
      <div className='flex h-[80vh] w-full flex-col items-center justify-center gap-4'>
        <EmptyState
          title='오늘의 기록이 없어요'
          description='새로운 순간을 기록해보세요'
        />
      </div>
    );

  return (
    <div className='flex w-full flex-col'>
      <div className='flex w-full flex-col gap-4'>
        {data.data.map((moment: MomentContent, index: number) => (
          <MomentCard
            key={moment.momentTime || `moment-${index}`}
            moment={moment}
            isToday={true}
          />
        ))}
      </div>
    </div>
  );
};

export default TodayPage;
