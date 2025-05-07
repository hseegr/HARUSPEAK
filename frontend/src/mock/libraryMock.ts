import { LibraryResponse } from '@/apis/libraryApi';

const generateMockDiaries = (
  count: number,
  startDate: Date,
): LibraryResponse['data'] => {
  return Array.from({ length: count }, (_, i) => {
    const date = new Date(startDate);
    date.setDate(date.getDate() - i);

    return {
      summaryId: i + 1,
      diaryDate: date.toISOString().split('T')[0],
      imageUrl: `https://picsum.photos/seed/${i}/400/300`,
      title: `하루 일기 ${i + 1}`,
      content: `오늘은 ${i + 1}번째 일기입니다. 이것은 일기의 내용입니다.`,
      isImageGenerating: false,
      imageGenerateCount: 0,
      contentGenerateCount: 0,
      momentCount: Math.floor(Math.random() * 10) + 1,
    };
  });
};

// 150개의 일기를 5개의 페이지로 나누어 생성 (각 페이지당 30개)
export const mockLibraryResponse: LibraryResponse = {
  data: generateMockDiaries(30, new Date()),
  resInfo: {
    dataCount: 30,
    nextCursor: 'cursor-2',
    hasMore: true,
  },
};

export const mockLibraryResponsePage2: LibraryResponse = {
  data: generateMockDiaries(
    30,
    new Date(new Date().setDate(new Date().getDate() - 30)),
  ),
  resInfo: {
    dataCount: 30,
    nextCursor: 'cursor-3',
    hasMore: true,
  },
};

export const mockLibraryResponsePage3: LibraryResponse = {
  data: generateMockDiaries(
    30,
    new Date(new Date().setDate(new Date().getDate() - 60)),
  ),
  resInfo: {
    dataCount: 30,
    nextCursor: 'cursor-4',
    hasMore: true,
  },
};

export const mockLibraryResponsePage4: LibraryResponse = {
  data: generateMockDiaries(
    30,
    new Date(new Date().setDate(new Date().getDate() - 90)),
  ),
  resInfo: {
    dataCount: 30,
    nextCursor: 'cursor-5',
    hasMore: true,
  },
};

export const mockLibraryResponsePage5: LibraryResponse = {
  data: generateMockDiaries(
    30,
    new Date(new Date().setDate(new Date().getDate() - 120)),
  ),
  resInfo: {
    dataCount: 30,
    nextCursor: '',
    hasMore: false,
  },
};
