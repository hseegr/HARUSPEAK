import { useMomentTagRecommend } from '@/hooks/useTodayQuery';
import { MomentContent } from '@/types/common';

interface AutoTagGeneratorProps {
  moment: MomentContent;
  initialTags: string[];
  isToday?: boolean;
  hideWhenDisabled?: boolean;
  buttonStyle?: 'default' | 'simple';
  isEditPage?: boolean;
  onTagsUpdate?: (newTags: string[]) => void;
}

const AutoTagGenerator = ({
  moment,
  initialTags,
  isToday = true,
  hideWhenDisabled = true,
  buttonStyle = 'default',
  isEditPage = false,
  onTagsUpdate,
}: AutoTagGeneratorProps) => {
  const { mutate: recommendTagMutation, isPending } =
    useMomentTagRecommend(isEditPage);

  const isButtonEnabled =
    isToday &&
    initialTags.length < 3 &&
    moment.content !== '' &&
    !initialTags.includes('아무말');

  if (hideWhenDisabled && !isButtonEnabled) return null;

  const buttonClassName =
    buttonStyle === 'default'
      ? 'rounded-full bg-haru-light-green hover:bg-haru-green px-4 py-1.5 text-sm font-bold text-white disabled:cursor-not-allowed disabled:opacity-50'
      : 'text-sm font-bold text-haru-light-green hover:text-haru-green disabled:cursor-not-allowed disabled:opacity-50';
  return (
    <div className='flex-shrink-0 pl-1'>
      <button
        onClick={() =>
          recommendTagMutation(
            {
              tags: initialTags,
              createdAt: moment.createdAt || moment.momentTime,
              content: moment.content,
              isEditPage: isEditPage,
            },
            {
              onSuccess: response => {
                if (onTagsUpdate) {
                  onTagsUpdate(response.recommendTags);
                }
              },
            },
          )
        }
        className={buttonClassName}
        aria-label='AI 태그 자동 생성'
        disabled={isPending || !isButtonEnabled}
      >
        {isPending ? '생성 중...' : '태그 자동 생성'}
      </button>
    </div>
  );
};

export default AutoTagGenerator;
