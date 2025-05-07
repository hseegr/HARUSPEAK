import { useGetImage } from '@/hooks/useDiaryQuery';

interface ThumbnailProps {
  summaryId: string;
}

const Thumbnail = ({ summaryId }: ThumbnailProps) => {
  const { data } = useGetImage(summaryId);

  const imageUrl = data?.imageUrl;
  return (
    <div>
      <img src={imageUrl} alt='thumbnail' />
    </div>
  );
};
export default Thumbnail;
