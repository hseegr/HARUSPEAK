interface ImageAttachButtonProps {
  onClick: () => void;
}

const ImageAttachButton = ({ onClick }: ImageAttachButtonProps) => {
  return (
    <button
      onClick={onClick}
      className='rounded-full border border-haru-green bg-haru-yellow px-3 py-1 text-xs font-semibold text-haru-green'
    >
      이미지 첨부
    </button>
  );
};

export default ImageAttachButton;
