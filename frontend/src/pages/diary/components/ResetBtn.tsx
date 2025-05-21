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
      <button
        onClick={onReset}
        disabled={isDisabled || generateCount >= 3}
        className='flex w-full items-center gap-1 rounded-full bg-haru-light-green px-3 py-1.5 text-sm text-white hover:bg-haru-green hover:bg-opacity-70 disabled:cursor-not-allowed disabled:bg-haru-gray-4 disabled:text-haru-gray-2'
      >
        <RotateCcw size={18} />
        {label} 다시 만들기 ({generateCount}/3)
      </button>
    </div>
  );
};

export default ResetBtn;
