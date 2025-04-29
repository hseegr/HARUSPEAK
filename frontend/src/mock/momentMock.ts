import { Moments, TodayDiaryResponse } from '@/types/today';

export const mockMoments: Moments = {
  '2025-04-28T10:00:00': {
    momentTime: '2025-04-28T10:00:00',
    images: [
      'https://img.vogue.co.kr/vogue/2023/12/style_657ff6f175a7f-1126x1400.jpg',
      'https://img.vogue.co.kr/vogue/2023/12/style_657ff6f175a7f-1126x1400.jpg',
      'https://img.vogue.co.kr/vogue/2023/12/style_657ff6f175a7f-1126x1400.jpg',
      'https://img.vogue.co.kr/vogue/2023/12/style_657ff6f175a7f-1126x1400.jpg',
      'https://img.vogue.co.kr/vogue/2023/12/style_657ff6f175a7f-1126x1400.jpg',
    ],
    content: '오늘은 날씨가 참 좋았다.',
    tags: ['산책', '햇살'],
  },
  '2025-04-28T11:00:00': {
    momentTime: '2025-04-28T11:00:00',
    images: [
      'https://img.vogue.co.kr/vogue/2023/12/style_657ff6f175a7f-1126x1400.jpg',
      'https://img.vogue.co.kr/vogue/2023/12/style_657ff6f175a7f-1126x1400.jpg',
      'https://img.vogue.co.kr/vogue/2023/12/style_657ff6f175a7f-1126x1400.jpg',
    ],
    content: '벚꽃 구경을 가기 딱 좋은 날씨였다.',
    tags: ['벚꽃8자넘는태그'],
  },
  '2025-04-28T17:00:00': {
    momentTime: '2025-04-28T17:00:00',
    images: [],
    content: '그렇지만 벚꽃 구경을 갈 여유가 없었다.',
    tags: [],
  },
};

export const mockTodayDiaryResponse: TodayDiaryResponse = {
  data: mockMoments,
  dataCount: Object.keys(mockMoments).length,
};
