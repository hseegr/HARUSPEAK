import { useGetImage } from '@/hooks/useDiaryQuery';

import ResetBtn from './ResetBtn';

interface ThumbnailProps {
  summaryId: string;
}

const Thumbnail = ({ summaryId }: ThumbnailProps) => {
  const { data } = useGetImage(summaryId);

  const imageUrl = data?.imageUrl;
  return (
    <div>
      <img src={imageUrl} alt='thumbnail' />
      <ResetBtn />
    </div>
  );
};
export default Thumbnail;
