import { TodayWriteStore } from '@/store/todayWriteStore';

import ImageItem from './ImageItem';

const ImageInputList = () => {
  const images = TodayWriteStore(state => state.images);
  // 이미지가 없으면 렌더링 하지 않음
  if (images.length === 0) return null;

  return (
    <div className='grid grid-cols-3 gap-2 px-1 pb-4'>
      {images.map((file, index) => (
        <ImageItem key={index} file={file} index={index} />
      ))}
    </div>
  );
};

export default ImageInputList;
