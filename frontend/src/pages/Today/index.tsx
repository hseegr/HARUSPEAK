import MomentCard from '@/components/MomentCard';
import { useToday } from '@/hooks/useToday';

const TodayPage = () => {
  const { data, isLoading } = useToday();

  if (isLoading) return <div>로딩중...</div>;
  if (!data) return <div>오늘의 기록이 없어요. 기록을 만들어 주세요.</div>;

  const firstMomentKey = Object.keys(data.data)[0];
  const formattedDate = firstMomentKey
    ? firstMomentKey.split('T')[0].replace(/-/g, '.')
    : '';

  return (
    <div className='flex w-full flex-col'>
      <div className='mb-4 text-end'>{formattedDate}</div>
      <div className='flex w-full flex-col gap-5'>
        {Object.values(data?.data || {}).map(moment => (
          <MomentCard key={moment.momentTime} moment={moment} isToday={true} />
        ))}
      </div>
    </div>
  );
};

export default TodayPage;
