// TypeScript는 JSX 문법을 사용하면 내부적으로 React.createElement를 호출한다고 가정함.
// 그래서 React 변수가 필요.
import React from 'react';
import { useEffect, useRef, useState } from 'react';

import { ChevronUp } from 'lucide-react';

import { TodayWriteStore } from '@/store/todayWriteStore';

const TextInput = () => {
  const [text, setText] = useState('');
  const textareaRef = useRef<HTMLTextAreaElement>(null);
  const addTextBlock = TodayWriteStore(state => state.addTextBlock);

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
    addTextBlock(text.trim());
    setText('');
  };

  return (
    <div className='flex min-h-20 w-full rounded-xl border px-2 py-2 focus-within:border-haru-green'>
      {/* 텍스트 입력창 */}
      <textarea
        ref={textareaRef}
        value={text}
        onChange={handleChange}
        placeholder='오늘 일기를 작성해주세요'
        rows={1}
        className='flex-grow resize-none bg-transparent text-sm placeholder-gray-400 focus:outline-none'
      />

      {/* 보내기 버튼 */}
      <button
        onClick={handleSubmit}
        disabled={!text.trim()}
        className={`ml-2 flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full ${text.trim() ? 'bg-haru-green' : 'bg-gray-300'}`}
      >
        <ChevronUp className='h-4 w-4 text-white' />
      </button>
    </div>
  );
};

export default TextInput;
