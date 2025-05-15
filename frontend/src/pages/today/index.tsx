import MomentCard from '@/components/MomentCard';
import { useToday } from '@/hooks/useTodayQuery';
import { formatDate } from '@/lib/timeUtils';
import { MomentContent } from '@/types/common';

const TodayPage = () => {
  const { data, isLoading } = useToday();

  if (isLoading) return <div>로딩중...</div>;
  if (!data?.data || data.dataCount === 0)
    return (
      <div className='flex h-[80vh] w-full flex-col items-center justify-center gap-4'>
        <div className='text-center'>
          <h2 className='mb-2 text-xl font-semibold text-haru-green'>
            오늘의 기록이 없어요
          </h2>
          <p className='text-haru-light-green'>새로운 순간을 기록해보세요</p>
        </div>
      </div>
    );

  const firstMoment = data.data[0];
  const formattedDate = firstMoment.createdAt
    ? formatDate(firstMoment.createdAt)
    : '';

  return (
    <div className='flex w-full flex-col'>
      <div className='mb-2'>{formattedDate}</div>
      <div className='flex w-full flex-col gap-3'>
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
