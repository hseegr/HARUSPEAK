import MomentCard from '@/components/MomentCard';
import { useToday } from '@/hooks/useTodayQuery';
import { formatDate } from '@/lib/timeUtils';
import { MomentContent } from '@/types/common';

const TodayPage = () => {
  const { data, isLoading } = useToday();

  if (isLoading) return <div>로딩중...</div>;
  if (!data?.data || data.dataCount === 0)
    return (
      <div>
        <span>오늘의 기록이 없어요. 기록을 만들어 주세요.</span>
      </div>
    );

  const firstMoment = data.data[0];
  const formattedDate = firstMoment.createdAt
    ? formatDate(firstMoment.createdAt)
    : '';

  return (
    <div className='flex flex-col w-full'>
      <div className='mb-4 text-end'>{formattedDate}</div>
      <div className='flex flex-col w-full gap-5'>
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
