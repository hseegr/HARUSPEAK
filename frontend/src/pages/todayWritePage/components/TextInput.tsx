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

  // 텍스트 입력창 높이 자동 조절
  const handleResizeHeight = () => {
    const textarea = textareaRef.current;
    if (textarea) {
      textarea.style.height = 'auto';
      textarea.style.height = `${textarea.scrollHeight}px`;
    }
  };

  // 텍스트 입력창 값 변경
  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newText = e.target.value;
    if (newText.length > 500) {
      return;
    }
    setText(newText);
  };

  // 텍스트 입력창 높이 자동 조절
  useEffect(() => {
    handleResizeHeight();
  }, [text]);

  // 텍스트 저장
  const handleSubmit = () => {
    if (!text.trim()) return;
    addTextBlock(text.trim());
    setText('');
  };

  // 모바일 환경 확인
  const isMobile = /iPhone|iPad|iPod|Android/i.test(navigator.userAgent);

  // 엔터 키 입력 시 텍스트 저장
  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey && !isMobile) {
      e.preventDefault();
      handleSubmit();
    }
  };

  return (
    <div className='flex min-h-20 w-full rounded-xl border px-2 py-2 focus-within:border-haru-green'>
      {/* 텍스트 입력창 */}
      <textarea
        ref={textareaRef}
        value={text}
        onChange={handleChange}
        onKeyDown={handleKeyDown}
        placeholder='오늘 무슨 일이 있었나요?'
        rows={1}
        className='flex-grow resize-none overflow-y-auto bg-transparent text-base placeholder-gray-400 focus:outline-none'
        style={{ maxHeight: '120px' }}
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
