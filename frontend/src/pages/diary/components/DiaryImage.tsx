import ImageSkeleton from '@/components/ImageSkeleton';

import ResetBtn from './ResetBtn';

interface DiaryImageProps {
  imageUrl: string | undefined;
  isImageGenerating: boolean;
  isImageRegenerating: boolean;
  isPending: boolean;
  generateCount: number;
  isEditing: boolean;
  onImageReset: () => void;
}

const DiaryImage = ({
  imageUrl,
  isImageGenerating,
  isImageRegenerating,
  isPending,
  generateCount,
  isEditing,
  onImageReset,
}: DiaryImageProps) => {
  const shouldShowImageSkeleton =
    isImageRegenerating || isImageGenerating || !imageUrl;

  return (
    <div className='relative'>
      {shouldShowImageSkeleton ? (
        <ImageSkeleton />
      ) : (
        <img className='rounded-xl' src={imageUrl} alt='하루 요약 이미지' />
      )}
      <div className='absolute bottom-2 right-2'>
        <ResetBtn
          generateCount={generateCount}
          onReset={onImageReset}
          isDisabled={
            isImageRegenerating || isPending || isImageGenerating || isEditing
          }
          type='image'
        />
      </div>
    </div>
  );
};

export default DiaryImage;
