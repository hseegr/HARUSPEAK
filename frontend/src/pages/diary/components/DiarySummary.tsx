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
}: DiarySummaryProps) => {
  return (
    <div className='relative'>
      <ContentSummary
        summary={isEditing ? editContent : content}
        isLoading={isContentRegenerating}
        isEditing={isEditing}
        onSummaryChange={onContentChange}
      />

      {/* 재생성 버튼은 수정 모드가 아닐 때만 표시 */}
      {!isEditing && (
        <div className='absolute bottom-2 right-2'>
          <ResetBtn
            generateCount={generateCount}
            onReset={onContentReset}
            isDisabled={isContentRegenerating || isPending}
            type='content'
          />
        </div>
      )}
    </div>
  );
};

export default DiarySummary;
