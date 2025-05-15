import { useState } from 'react';

import { defaultEmojis } from '@/types/common';

interface EmojiSelectorProps {
  isOpen: boolean;
  onClose: () => void;
  onSelect: (emojis: string[]) => void;
  selectedEmojis: string[];
}

const EmojiSelector = ({
  isOpen,
  onClose,
  onSelect,
  selectedEmojis,
}: EmojiSelectorProps) => {
  const [tempSelected, setTempSelected] = useState<string[]>(selectedEmojis);

  if (!isOpen) return null;

  const handleToggleEmoji = (emoji: string) => {
    setTempSelected(prev =>
      prev.includes(emoji) ? prev.filter(e => e !== emoji) : [...prev, emoji],
    );
  };

  const handleToggleAll = () => {
    setTempSelected(prev =>
      prev.length === defaultEmojis.length ? [] : [...defaultEmojis],
    );
  };

  const handleSave = () => {
    onSelect(tempSelected);
    onClose();
  };

  return (
    <div className='fixed inset-0 z-[9999] flex items-center justify-center bg-black bg-opacity-50'>
      <div className='w-96 rounded-lg bg-white p-6'>
        <div className='mb-4 flex items-center justify-between'>
          <h2 className='text-xl font-bold'>이모지 선택</h2>
          <button
            onClick={handleToggleAll}
            className='text-sm text-haru-gray-5 transition-colors hover:text-haru-green'
          >
            {tempSelected.length === defaultEmojis.length
              ? '전체 해제'
              : '전체 선택'}
          </button>
        </div>
        <div className='mb-4 grid grid-cols-5 gap-2'>
          {defaultEmojis.map(emoji => (
            <button
              key={emoji}
              onClick={() => handleToggleEmoji(emoji)}
              className={`flex h-12 w-12 items-center justify-center rounded-lg text-2xl transition-all duration-200 ease-in-out ${
                tempSelected.includes(emoji)
                  ? 'bg-haru-green text-white hover:bg-haru-light-green'
                  : 'bg-gray-100 hover:bg-haru-light-green'
              }`}
            >
              {emoji}
            </button>
          ))}
        </div>
        <div className='flex justify-end gap-2'>
          <button
            onClick={handleSave}
            disabled={tempSelected.length === 0}
            className={`rounded-lg px-4 py-2 text-white transition-all duration-200 ease-in-out ${
              tempSelected.length === 0
                ? 'cursor-not-allowed bg-gray-300'
                : 'bg-haru-light-green hover:bg-haru-green'
            }`}
          >
            {tempSelected.length === 0 ? '이모지를 선택해주세요' : '저장'}
          </button>
          <button
            onClick={onClose}
            className='rounded-lg bg-gray-200 px-4 py-2 transition-colors duration-200 ease-in-out hover:bg-gray-300'
          >
            취소
          </button>
        </div>
      </div>
    </div>
  );
};

export default EmojiSelector;
