interface ContentSummaryProps {
  summary: string;
  isLoading?: boolean;
  isEditing?: boolean;
  onSummaryChange?: (value: string) => void;
}

const ContentSummary = ({
  summary,
  isLoading,
  isEditing = false,
  onSummaryChange,
}: ContentSummaryProps) => {
  if (isLoading) {
    return (
      <div className='flex h-32 animate-pulse items-center justify-center rounded-xl bg-haru-gray-3 p-4'>
        생성 중...
      </div>
    );
  }

  if (isEditing) {
    return (
      <div className='rounded-xl bg-haru-beige p-4'>
        <textarea
          className='h-full min-h-32 w-full resize-none bg-haru-light-green font-leeseyoon text-white focus:outline-none'
          value={summary}
          onChange={e => onSummaryChange?.(e.target.value)}
          autoFocus
        />
      </div>
    );
  }

  return (
    <div className='rounded-xl bg-haru-beige p-4 pb-12'>
      <p className='whitespace-pre-wrap font-leeseyoon'>{summary}</p>
    </div>
  );
};

export default ContentSummary;
