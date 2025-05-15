import { create } from 'zustand';

import { defaultEmojis } from '@/types/common';

interface EmojiState {
  selectedEmojis: string[];
  setSelectedEmojis: (emojis: string[]) => void;
}

// localStorage에서 저장된 이모지 가져오기
const getInitialEmojis = () => {
  if (typeof window === 'undefined') return defaultEmojis;
  const savedEmojis = localStorage.getItem('selectedEmojis');
  return savedEmojis ? JSON.parse(savedEmojis) : defaultEmojis;
};

export const useEmojiStore = create<EmojiState>()(set => ({
  selectedEmojis: getInitialEmojis(),
  setSelectedEmojis: emojis => {
    localStorage.setItem('selectedEmojis', JSON.stringify(emojis));
    set({ selectedEmojis: emojis });
  },
}));
