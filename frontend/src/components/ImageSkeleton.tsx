interface ImageSkeletonProps {
  message?: string;
}

const ImageSkeleton = ({ message }: ImageSkeletonProps) => {
  return (
    <div className='relative aspect-square w-full rounded-xl bg-haru-gray-3'>
      <div className='absolute left-1/2 top-1/2 flex -translate-x-1/2 -translate-y-1/2 flex-col items-center'>
        <div className='mb-4 h-12 w-12 animate-spin rounded-full border-4 border-haru-gray-5 border-t-haru-light-green'></div>
        {message && <p className='text-center text-haru-gray-5'>{message}</p>}
      </div>
    </div>
  );
};

export default ImageSkeleton;
