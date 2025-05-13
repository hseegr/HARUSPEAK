import { useMomentTagRecommend } from '@/hooks/useMomentTagRecommend';
import { MomentContent } from '@/types/common';

interface AutoTagGeneratorProps {
  moment: MomentContent;
  initialTags: string[];
  isToday?: boolean;
  hideWhenDisabled?: boolean;
  buttonStyle?: 'default' | 'simple';
}

const AutoTagGenerator = ({
  moment,
  initialTags,
  isToday = true,
  hideWhenDisabled = true,
  buttonStyle = 'default',
}: AutoTagGeneratorProps) => {
  const { tags, isLoading, handleGenerateTags } = useMomentTagRecommend({
    moment,
    initialTags,
  });

  const isButtonEnabled =
    isToday &&
    tags.length < 3 &&
    moment.content !== '' &&
    !tags.includes('아무말');

  if (hideWhenDisabled && !isButtonEnabled) return null;

  const buttonClassName =
    buttonStyle === 'default'
      ? 'rounded-full bg-[#41644A] px-4 py-1.5 text-sm font-bold text-white disabled:cursor-not-allowed disabled:opacity-50'
      : 'text-sm font-bold text-[#41644A] disabled:cursor-not-allowed disabled:opacity-50';

  return (
    <div className='flex-shrink-0 pl-1'>
      <button
        onClick={handleGenerateTags}
        className={buttonClassName}
        aria-label='AI 태그 자동 생성'
        disabled={isLoading || !isButtonEnabled}
      >
        {isLoading ? '생성 중...' : '태그 자동 생성'}
      </button>
    </div>
  );
};

export default AutoTagGenerator;
