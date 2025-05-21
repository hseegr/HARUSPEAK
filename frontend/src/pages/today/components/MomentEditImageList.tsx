interface MomentEditImageListProps {
  images: string[];
  onDeleteImage: (index: number) => void;
}

const MomentEditImageList = ({
  images,
  onDeleteImage,
}: MomentEditImageListProps) => {
  if (images.length === 0) return null;

  return (
    <div className='max-h-[250px] overflow-y-auto'>
      <div
        className={`grid ${images.length >= 4 ? 'grid-cols-5' : 'grid-cols-3'} gap-2 p-1`}
      >
        {Array.from({ length: images.length }).map((_, idx) => (
          <div
            key={idx}
            className='relative aspect-square w-full rounded-lg bg-haru-gray-2'
          >
            {images[idx] ? (
              <>
                <img
                  src={images[idx]}
                  alt={`image-${idx}`}
                  className='h-full w-full rounded-lg object-cover'
                />
                <button
                  onClick={() => onDeleteImage(idx)}
                  className='absolute right-1 top-1 rounded-full bg-haru-black bg-opacity-50 p-0.5 px-1 text-xs text-white hover:text-red-500'
                >
                  ✕
                </button>
              </>
            ) : null}
          </div>
        ))}
      </div>
    </div>
  );
};

export default MomentEditImageList;
