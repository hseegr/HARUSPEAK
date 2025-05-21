interface DiaryDeleteBtnProps {
  onClick: () => void;
  isImageRegenerating: boolean;
}

const DiaryDeleteBtn = ({
  onClick,
  isImageRegenerating,
}: DiaryDeleteBtnProps) => {
  return (
    <button
      onClick={onClick}
      className='px-2 py-1 font-mont text-haru-gray-4 hover:text-haru-green disabled:cursor-not-allowed disabled:text-haru-gray-5'
      disabled={isImageRegenerating}
    >
      삭제
    </button>
  );
};

export default DiaryDeleteBtn;
