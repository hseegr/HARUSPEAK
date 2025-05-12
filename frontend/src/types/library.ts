export interface GetLibraryParams {
  limit?: number;
  before?: string;
  startDate?: string;
  endDate?: string;
}

export interface BaseDiary {
  summaryId: number;
  diaryDate: string;
  imageUrl: string;
  title: string;
  content: string;
  imageGenerateCount: number;
  contentGenerateCount: number;
  momentCount: number;
}

export interface Diary extends BaseDiary {
  isImageGenerating: boolean;
}

export interface ResInfo {
  dataCount: number;
  nextCursor: string;
  hasMore: boolean;
}

export interface LibraryResponse {
  data: Diary[];
  resInfo: ResInfo;
}

export interface LibraryParams {
  limit?: number;
  before?: string;
  startDate?: string;
  endDate?: string;
  userTags?: string;
}
