// 이미지 그리드 컴포넌트 - 최대 3개의 이미지를 표시하고 이미지 다이얼로그를 처리
import { useState } from 'react';

import ImageDialog from '@/components/ImageDialog';

interface ImageGridProps {
  images: string[];
  momentTime: string;
  momentId?: number;
}

const ImageGrid = ({ images, momentTime, momentId }: ImageGridProps) => {
  const [isImageDialogOpen, setIsImageDialogOpen] = useState(false);
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);

  if (images.length === 0) return null;

  return (
    <>
      <div className='mb-3 flex w-full gap-1'>
        {images.slice(0, 3).map((image, idx) => {
          const remainingCount = images.length - 2;

          return (
            <div
              key={`${image}-${idx}`}
              className='relative aspect-square w-1/3 cursor-pointer overflow-hidden rounded-xl'
              onClick={e => {
                e.stopPropagation(); // 이벤트 전파 중지
                setSelectedImageIndex(idx);
                setIsImageDialogOpen(true);
              }}
            >
              <img
                src={image}
                alt={`moment-${momentId}-img-${idx}`}
                onError={e => {
                  e.currentTarget.src = '/fallback-image.png';
                }}
                className={`h-full w-full object-cover ${idx === 2 && remainingCount > 0 ? 'blur-sm' : ''}`}
              />
              {idx === 2 && remainingCount > 0 && (
                <div className='absolute inset-0 flex items-center justify-center bg-black bg-opacity-50 text-lg font-bold text-white'>
                  +{remainingCount}
                </div>
              )}
            </div>
          );
        })}
        {/* 빈 공간 채우기 */}
        {images.length === 1 && (
          <>
            <div className='w-1/3'></div>
            <div className='w-1/3'></div>
          </>
        )}
        {images.length === 2 && <div className='w-1/3'></div>}
      </div>

      <ImageDialog
        open={isImageDialogOpen}
        onOpenChange={setIsImageDialogOpen}
        images={images}
        currentIndex={selectedImageIndex}
        momentTime={momentTime}
      />
    </>
  );
};

export default ImageGrid;
