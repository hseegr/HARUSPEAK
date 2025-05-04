import { RotateCcw } from 'lucide-react';

const ResetBtn = () => {
  return (
    <div>
      {/* 재요청 버튼 누르면 이미지 재생성 or`요약 재생성 */}
      {/* 응답값에 따라서 UI 변경되어야 함 -> 변경되는게 아니라 항상 1/3 스타일로 있어야하나 */}
      <button>
        <RotateCcw size={20} />
        {/* 다시 만들기 (1/3) */}
      </button>
    </div>
  );
};

export default ResetBtn;
