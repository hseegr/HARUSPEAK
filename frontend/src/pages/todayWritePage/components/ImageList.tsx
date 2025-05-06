import { X } from 'lucide-react';

interface ImageListProps {
  images: File[];
  onRemove: (index: number) => void;
}

const ImageList = ({ images, onRemove }: ImageListProps) => {
  return (
    <div className='flex w-full flex-col gap-2'>
      {images.map((file, index) => (
        <div
          key={index}
          className='flex items-center justify-between rounded-lg border bg-white px-2 py-2 text-sm text-gray-700'
        >
          {/* 파일 이름 */}
          <span className='max-w-[80%] truncate'>{file.name}</span>

          {/* 삭제 버튼 */}
          <button
            onClick={() => onRemove(index)}
            className='text-gray-400 hover:text-red-400'
          >
            <X size={16} />
          </button>
        </div>
      ))}
    </div>
  );
};

export default ImageList;
