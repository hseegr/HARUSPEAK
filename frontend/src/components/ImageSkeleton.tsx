interface ImageSkeletonProps {
  message?: string;
}

const ImageSkeleton = ({ message }: ImageSkeletonProps) => {
  return (
    <div className='flex h-[320px] w-full flex-col items-center justify-center rounded-xl bg-haru-gray-3 p-4'>
      <div className='mb-4 h-12 w-12 animate-spin rounded-full border-4 border-haru-gray-5 border-t-haru-light-green'></div>
      <p className='text-center text-haru-gray-5'>{message}</p>
    </div>
  );
};

export default ImageSkeleton;
