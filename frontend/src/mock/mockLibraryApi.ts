import { GetLibraryParams, LibraryResponse } from '@/apis/libraryApi';

import {
  mockLibraryResponse,
  mockLibraryResponsePage2,
  mockLibraryResponsePage3,
  mockLibraryResponsePage4,
  mockLibraryResponsePage5,
} from './libraryMock';

export const mockGetLibrary = async (
  params: GetLibraryParams,
): Promise<LibraryResponse> => {
  // 페이지네이션 시뮬레이션
  if (!params.before) {
    return mockLibraryResponse;
  }
  if (params.before === 'cursor-2') {
    return mockLibraryResponsePage2;
  }
  if (params.before === 'cursor-3') {
    return mockLibraryResponsePage3;
  }
  if (params.before === 'cursor-4') {
    return mockLibraryResponsePage4;
  }
  if (params.before === 'cursor-5') {
    return mockLibraryResponsePage5;
  }

  // 필터링 시뮬레이션
  if (params.startDate || params.endDate) {
    const filteredData = mockLibraryResponse.data.filter(diary => {
      if (params.startDate && diary.diaryDate < params.startDate) return false;
      if (params.endDate && diary.diaryDate > params.endDate) return false;
      return true;
    });

    return {
      data: filteredData,
      resInfo: {
        dataCount: filteredData.length,
        nextCursor: '',
        hasMore: false,
      },
    };
  }

  return mockLibraryResponse;
};
