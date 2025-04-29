import { useEffect, useRef, useState } from 'react';

import { ChevronUp } from 'lucide-react';

// 보내기 아이콘

const TextInput = () => {
  const [text, setText] = useState('');
  const textareaRef = useRef<HTMLTextAreaElement>(null);

  const handleResizeHeight = () => {
    const textarea = textareaRef.current;
    if (textarea) {
      textarea.style.height = 'auto';
      textarea.style.height = `${textarea.scrollHeight}px`;
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setText(e.target.value);
  };

  useEffect(() => {
    handleResizeHeight();
  }, [text]);

  const handleSubmit = () => {
    if (!text.trim()) return;
    setText('');
  };

  return (
    <div className='relative w-full'>
      {/* 텍스트 입력창 */}
      <textarea
        ref={textareaRef}
        value={text}
        onChange={handleChange}
        placeholder='오늘 일기를 작성해주세요'
        className='min-h-[120px] w-full resize-none rounded-xl border p-3 pr-12 text-sm placeholder-gray-400 focus:border-haru-green focus:outline-none'
      />

      {/* 보내기 버튼 */}
      <button
        onClick={handleSubmit}
        className={`absolute bottom-4 right-4 flex h-8 w-8 items-center justify-center rounded-full ${text.trim() ? 'bg-haru-green' : 'bg-gray-300'}`}
      >
        <ChevronUp className='h-4 w-4 text-white' />
      </button>
    </div>
  );
};

export default TextInput;
