// 클릭 시 제목, 요약 내용 수정 가능
interface ContentEditBtnProps {
  onClick: () => void;
  isEditing: boolean;
}

const ContentEditBtn = ({ onClick, isEditing }: ContentEditBtnProps) => {
  return (
    <div>
      <div
        onClick={onClick}
        className={`cursor-pointer px-1 py-1 font-mont ${isEditing ? 'text-haru-green' : 'text-haru-gray-4 hover:text-haru-green'}`}
      >
        수정
      </div>
    </div>
  );
};

export default ContentEditBtn;
