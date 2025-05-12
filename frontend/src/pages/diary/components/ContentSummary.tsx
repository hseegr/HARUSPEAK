interface ContentSummaryProps {
  summary: string;
  isLoading?: boolean;
}

const ContentSummary = ({ summary, isLoading }: ContentSummaryProps) => {
  if (isLoading) {
    return (
      <div className='flex h-32 animate-pulse items-center justify-center rounded-xl bg-haru-gray-3 p-4'>
        생성 중...
      </div>
    );
  }

  return (
    <div className='rounded-xl bg-haru-beige p-4 pb-12'>
      <p className='whitespace-pre-wrap'>{summary}</p>
    </div>
  );
};

export default ContentSummary;
