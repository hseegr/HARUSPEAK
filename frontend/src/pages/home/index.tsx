import SplashScreen from '@/components/SplashScreen';
import TodayMoments from '@/components/TodayMoments';
import { useHomeStatistics } from '@/hooks/useHomeStatistics';
import useAuthStore from '@/store/userStore';

const Home = () => {
  const { user } = useAuthStore();
  const { statistics, isLoading, error, refetch, finishLoading } =
    useHomeStatistics();

  if (isLoading) {
    return <SplashScreen onFinish={finishLoading} />;
  }

  if (error) {
    return (
      <div className='flex min-h-[80vh] w-full flex-col items-center justify-center p-4'>
        <p>{error}</p>
        <button
          onClick={refetch}
          className='mt-4 rounded-md bg-haru-green px-4 py-2 text-white hover:bg-haru-green/80'
        >
          다시 시도
        </button>
      </div>
    );
  }

  return (
    <div className='flex min-h-[80vh] w-full flex-col'>
      <div className='flex flex-1 flex-col justify-between gap-8'>
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
            <section className='min-h-96 w-full flex-1 items-center rounded-lg'>
              <TodayMoments momentCount={statistics.todayCount} />
              <p className='w-full text-center font-leeseyoon text-haru-gray-5'>
                <span className='mr-1'>오늘</span>
                <span className='font-bold text-haru-green'>
                  {statistics.todayCount}
                </span>
                <span>개의 순간을 기록했어요</span>
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
                  {statistics.totalDiaryCount}일
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
