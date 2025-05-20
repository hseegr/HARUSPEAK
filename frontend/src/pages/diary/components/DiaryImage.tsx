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
  hasContent: boolean;
}

const DiaryImage = ({
  imageUrl,
  isImageGenerating,
  isImageRegenerating,
  isPending,
  generateCount,
  isEditing,
  onImageReset,
  hasContent,
}: DiaryImageProps) => {
  const shouldShowImageSkeleton =
    isImageRegenerating || isImageGenerating || !imageUrl;

  return (
    <div className='relative'>
      {shouldShowImageSkeleton ? (
        <ImageSkeleton />
      ) : (
        <img
          className='h-full w-full rounded-xl object-cover'
          src={imageUrl}
          alt='하루 요약 이미지'
        />
      )}
      {!isEditing && hasContent && (
        <div className='absolute bottom-2 right-2'>
          <ResetBtn
            generateCount={generateCount}
            onReset={onImageReset}
            isDisabled={
              isImageRegenerating ||
              isPending ||
              isImageGenerating ||
              isEditing ||
              !hasContent
            }
            type='image'
          />
        </div>
      )}
    </div>
  );
};

export default DiaryImage;
