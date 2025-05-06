import { create } from 'zustand';

import { TodayWriteRequest } from '@/types/todayWrite';

// 오늘 덩어리 저장 스토어
export const TodayWriteStore = create<TodayWriteRequest>(set => ({
  images: [],
  textBlocks: [],

  // 이미지
  addImages: file => set(state => ({ images: [...state.images, file] })),
  removeImages: index =>
    set(state => ({ images: state.images.filter((_, i) => i !== index) })),
  clearImages: () => set({ images: [] }),

  // 텍스트
  addTextBlock: text =>
    set(state => ({ textBlocks: [...state.textBlocks, text] })),
  removeTextBlock: index =>
    set(state => ({
      textBlocks: state.textBlocks.filter((_, i) => i !== index),
    })),
  clearTextBlocks: () => set({ textBlocks: [] }),

  // 텍스트 수정
  updateTextBlock: (index: number, text: string) =>
    set(state => {
      const updated = [...state.textBlocks];
      updated[index] = text;
      return { textBlocks: updated };
    }),

  // 모든 데이터 초기화
  clearAll: () =>
    set({
      images: [],
      textBlocks: [],
    }),
}));
