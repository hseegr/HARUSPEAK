interface MomentEditContentProps {
  content: string;
  contentLength: number;
  onContentChange: (value: string) => void;
}

const MomentEditContent = ({
  content,
  contentLength,
  onContentChange,
}: MomentEditContentProps) => {
  return (
    <div className='flex flex-col gap-1'>
      <textarea
        value={content}
        onChange={e => onContentChange(e.target.value)}
        className='text-md w-full rounded-md border border-gray-300 p-2 focus:outline-haru-green'
        placeholder='순간의 기록을 입력하세요'
        rows={3}
      />
      <div className='flex justify-end'>
        <span
          className={
            contentLength > 500 ? 'font-bold text-red-500' : 'text-gray-500'
          }
        >
          {contentLength}/500자
        </span>
      </div>
    </div>
  );
};

export default MomentEditContent;
