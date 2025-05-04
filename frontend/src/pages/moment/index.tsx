import { useParams } from 'react-router-dom';

import { useGetMoment } from '@/hooks/useMomentQuery';

// 순간 일기 상세 페이지
const Moment = () => {
  const { momentId } = useParams();
  const { data, isPending, isError } = useGetMoment(momentId || '');

  if (isPending) {
    return <div>Loading...</div>;
  }

  if (isError) {
    return <div>Error...</div>;
  }

  return (
    <div>
      <div>순간 일기 상세</div>
      <div>{data?.momentTime}</div>
      <div>{data?.images}</div>
      <div>{data?.content}</div>
      <div>{data?.tags}</div>
    </div>
  );
};

export default Moment;
