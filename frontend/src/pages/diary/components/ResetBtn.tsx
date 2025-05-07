import { RotateCcw } from 'lucide-react';

interface ResetBtnProps {
  generateCount: number;
  onReset: () => void;
  isDisabled?: boolean;
  type: 'image' | 'content';
}

const ResetBtn = ({
  generateCount,
  onReset,
  isDisabled = false,
  type,
}: ResetBtnProps) => {
  const label = type === 'image' ? '이미지' : '요약';

  return (
    <div>
      {/* 재요청 버튼 누르면 이미지 재생성 or 요약 재생성 */}
      {/* 응답값에 따라서 UI 변경되어야 함 -> 변경되는게 아니라 항상 1/3 스타일로 있어야하나 */}
      {/* 이미지든 요약 내용이든 아직 생성중이면 생성 중 UI 및 버튼 비활성화 */}

      {/* 재생성 3회 초과시 버튼 비활성화 */}
      {/* 재생성 요청 보내기 전에 확인 창 띄울지는 고민 중 */}
      <button
        onClick={onReset}
        disabled={isDisabled || generateCount >= 3}
        className='flex w-[200px] items-center gap-2 rounded-md px-3 py-2 text-sm disabled:opacity-50'
      >
        <RotateCcw size={20} />
        {label} 다시 만들기 ({generateCount}/3)
      </button>
    </div>
  );
};

export default ResetBtn;
