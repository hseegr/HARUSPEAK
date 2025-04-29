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
      resolve({
        recommendTags: [...request.tags, '추가추천태그'],
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
