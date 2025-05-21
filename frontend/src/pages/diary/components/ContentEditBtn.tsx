// 클릭 시 제목, 요약 내용 수정 가능
interface ContentEditBtnProps {
  onClick: () => void;
  isEditing: boolean;
  isContentRegenerating: boolean;
}

const ContentEditBtn = ({
  onClick,
  isEditing,
  isContentRegenerating,
}: ContentEditBtnProps) => {
  return (
    <button
      onClick={onClick}
      className={`cursor-pointer px-1 py-1 font-mont ${isEditing ? 'text-haru-green' : 'text-haru-gray-4 hover:text-haru-green'} disabled:cursor-not-allowed disabled:text-haru-gray-5`}
      disabled={isContentRegenerating}
    >
      수정
    </button>
  );
};

export default ContentEditBtn;
