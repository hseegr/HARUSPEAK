import { X } from 'lucide-react';

import { TodayWriteStore } from '@/store/todayWriteStore';

interface ImageItemProps {
  file: File;
  index: number;
}

const ImageItem = ({ file, index }: ImageItemProps) => {
  const removeImages = TodayWriteStore(state => state.removeImages);
  return (
    <div className='relative aspect-square overflow-hidden rounded bg-haru-gray-1'>
      <img
        src={URL.createObjectURL(file)}
        alt={`image-${index}`}
        className='h-full w-full object-cover'
      />
      <button
        onClick={() => removeImages(index)}
        className='absolute right-1 top-1 rounded-full bg-white bg-opacity-80 p-1'
      >
        <X className='h-4 w-4 text-haru-green' />
      </button>
    </div>
  );
};

export default ImageItem;
