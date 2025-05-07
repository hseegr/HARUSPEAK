import { mockMoments } from '@/mock/mockMomentData';

// 순간 일기 상세 조회
export const mockGetMoment = async (momentId: string) => {
  // 지연 시간 추가 (실제 API 호출처럼 보이게)
  await new Promise(resolve => setTimeout(resolve, 500));

  const moment = mockMoments.find(m => m.momentId === Number(momentId));

  if (!moment) {
    throw new Error('Moment not found');
  }

  return moment;
};

// 순간 일기 목록 조회 (필터링 포함)
export const mockGetMoments = async (params: {
  startDate?: string;
  endDate?: string;
  userTags?: string[];
  limit?: number;
  before?: string;
}) => {
  await new Promise(resolve => setTimeout(resolve, 500));

  let filteredMoments = [...mockMoments];

  // 날짜 필터링
  if (params.startDate) {
    filteredMoments = filteredMoments.filter(
      m => m.momentTime >= params.startDate!,
    );
  }
  if (params.endDate) {
    filteredMoments = filteredMoments.filter(
      m => m.momentTime <= params.endDate!,
    );
  }

  // 태그 필터링
  if (params.userTags?.length) {
    filteredMoments = filteredMoments.filter(m =>
      m.tags.some(tag => params.userTags!.includes(tag)),
    );
  }

  // 페이지네이션
  const limit = params.limit || 10;
  let moments = filteredMoments;

  if (params.before && params.before !== 'undefined') {
    moments = moments.filter(m => m.momentTime < params.before!);
  }

  moments = moments.slice(0, limit);

  return {
    data: moments,
    resInfo: {
      dataCount: moments.length,
      nextCursor:
        moments.length > 0 ? moments[moments.length - 1].momentTime : null,
      hasMore: moments.length === limit,
    },
  };
};

// 순간 일기 삭제
export const mockDeleteMoment = async (momentId: string) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  const index = mockMoments.findIndex(m => m.momentId === Number(momentId));
  if (index !== -1) mockMoments.splice(index, 1);
  return { success: true };
};
