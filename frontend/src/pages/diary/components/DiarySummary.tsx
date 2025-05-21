import ContentSummary from './ContentSummary';
import ResetBtn from './ResetBtn';

interface DiarySummaryProps {
  content: string;
  editContent: string;
  isEditing: boolean;
  isContentRegenerating: boolean;
  isPending: boolean;
  generateCount: number;
  onContentChange: (value: string) => void;
  onContentReset: () => void;
  hasContent: boolean;
  isImageGenerating: boolean;
}

const DiarySummary = ({
  content,
  editContent,
  isEditing,
  isContentRegenerating,
  isPending,
  generateCount,
  onContentChange,
  onContentReset,
  hasContent,
  isImageGenerating,
}: DiarySummaryProps) => {
  return (
    <div className='relative'>
      <ContentSummary
        summary={isEditing ? editContent : content}
        isLoading={isContentRegenerating}
        isEditing={isEditing}
        onSummaryChange={onContentChange}
        generateCount={generateCount}
      />

      {/* 재생성 버튼은 수정 모드가 아니거나 hasContent가 있을 때만 보임 */}
      {!isEditing && hasContent && (
        <div className='absolute bottom-2 right-2'>
          <ResetBtn
            generateCount={generateCount}
            onReset={onContentReset}
            isDisabled={
              isContentRegenerating ||
              isPending ||
              !hasContent ||
              isImageGenerating
            }
            type='content'
          />
        </div>
      )}
    </div>
  );
};

export default DiarySummary;
