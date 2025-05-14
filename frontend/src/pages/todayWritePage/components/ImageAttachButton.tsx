interface ImageAttachButtonProps {
  onClick: () => void;
}

const ImageAttachButton = ({ onClick }: ImageAttachButtonProps) => {
  return (
    <button
      onClick={onClick}
      className='rounded-full border border-haru-green bg-haru-yellow px-3 py-1 text-[13px] font-semibold text-haru-green hover:bg-haru-green hover:text-haru-yellow'
    >
      이미지 첨부
    </button>
  );
};

export default ImageAttachButton;
