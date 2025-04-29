import { mockTodayDiaryResponse } from '@/mock/momentMock';
import {
  TagRequest,
  TagResponse,
  TodayDiaryResponse,
  UpdateMomentRequest,
} from '@/types/today';

export const fetchTodayDiary = async (): Promise<TodayDiaryResponse> => {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve(mockTodayDiaryResponse);
    }, 500); // 네트워크 지연 흉내
  });
};

export const createTags = async (request: TagRequest): Promise<TagResponse> => {
  return new Promise(resolve => {
    setTimeout(() => {
      const currentTagCount = request.tags.length;
      const tagsNeeded = Math.max(0, 3 - currentTagCount);

      const possibleNewTags = [
        '추천태그1',
        '추천태그2',
        '추천태그3',
        '추천태그4',
        '추천태그5',
      ];

      const recommendTags = possibleNewTags.slice(0, tagsNeeded);

      resolve({
        recommendTags,
      });
    }, 300);
  });
};

export const updateMoment = async (
  moment: UpdateMomentRequest,
): Promise<void> => {
  return new Promise(resolve => {
    setTimeout(() => {
      console.log('updated moment:', moment);
      resolve();
    }, 300);
  });
};
