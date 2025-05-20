interface ContentSummaryProps {
  summary: string;
  isLoading?: boolean;
  isEditing?: boolean;
  onSummaryChange?: (value: string) => void;
  generateCount: number;
}

const ContentSummary = ({
  summary,
  isLoading,
  isEditing = false,
  onSummaryChange,
  generateCount,
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
      <div>
        <div className='mb-1 min-h-[1.25rem]'>
          {summary.length > 200 && (
            <p className='text-sm text-red-500'>
              일기 요약은 최대 200글자까지 작성 가능합니다
            </p>
          )}
        </div>
        <textarea
          className='w-full rounded-xl border border-haru-light-green bg-haru-yellow p-1 font-leeseyoon text-xl focus:outline-none'
          value={summary}
          onChange={e => onSummaryChange?.(e.target.value)}
          rows={4}
          autoFocus
        />
      </div>
    );
  }

  if (!isEditing && summary.trim() === '') {
    return (
      <div className='rounded-xl bg-haru-gray-3 px-8 py-12 text-center text-haru-gray-5'>
        {generateCount === 0
          ? `사진으로만 구성된 일기입니다`
          : `일기 요약이 없습니다`}
      </div>
    );
  }

  return (
    <div className='rounded-xl bg-haru-gray-3 p-4 pb-12'>
      <p
        className='whitespace-pre-wrap font-leeseyoon'
        style={{ whiteSpace: 'pre-wrap', wordBreak: 'break-all' }}
      >
        {summary}
      </p>
    </div>
  );
};

export default ContentSummary;
