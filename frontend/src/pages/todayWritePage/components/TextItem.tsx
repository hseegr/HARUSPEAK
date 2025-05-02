import React, { useEffect, useRef } from 'react';

import { X } from 'lucide-react';

import { TodayWriteStore } from '@/store/todayWriteStore';

interface TextItemProps {
  text: string;
  index: number;
}

const TextItem = ({ text, index }: TextItemProps) => {
  const updateTextBlock = TodayWriteStore(state => state.updateTextBlock);
  const removeTextBlock = TodayWriteStore(state => state.removeTextBlock);

  const textareaRef = useRef<HTMLTextAreaElement>(null);

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    updateTextBlock(index, e.target.value);
  };

  const handleRemove = () => {
    removeTextBlock(index);
  };

  // 텍스트 변경될 때마다 높이 자동 조절
  useEffect(() => {
    const textarea = textareaRef.current;
    if (textarea) {
      textarea.style.height = 'auto'; // 초기화 후
      textarea.style.height = `${textarea.scrollHeight}px`; // scrollHeight만큼 설정
    }
  }, [text]);

  return (
    <div className='relative'>
      <textarea
        ref={textareaRef}
        rows={1}
        className='w-full resize-none overflow-hidden rounded-xl border border-haru-gray-3 bg-haru-gray-1 p-3 text-sm text-haru-black'
        value={text}
        onChange={handleChange}
      />
      <button
        className='absolute right-2 top-2 text-gray-400 hover:text-red-500'
        onClick={handleRemove}
      >
        <X size={16} />
      </button>
    </div>
  );
};

export default TextItem;
