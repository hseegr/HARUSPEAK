interface ContentSummaryProps {
  summary: string;
}

const ContentSummary = ({ summary }: ContentSummaryProps) => {
  return (
    <div>
      <div>AI 내용 요약</div>
      <div className='h-[100px] w-full rounded-xl bg-haru-gray-3 px-3 py-2 text-sm text-white'>
        {summary}
      </div>
    </div>
  );
};

export default ContentSummary;
