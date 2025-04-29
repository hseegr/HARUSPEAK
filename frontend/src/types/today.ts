import { MomentContent } from '@/types/common';

export type Moments = Record<string, MomentContent>;

// 오늘의 일기 불러오기 API 응답 타입
export interface TodayDiaryResponse {
  data: Moments;
  dataCount: number;
}

// 태그 생성 API 요청 타입
export interface TagRequest {
  tags: string[];
  createdAt: string;
  content: string;
}

// 태그 생성 API 응답 타입
export interface TagResponse {
  recommendTags: string[];
}

// 오늘의 순간 일기 수정 API 요청 타입
export interface UpdateMomentRequest extends MomentContent {
  deleteImages: string[];
}
