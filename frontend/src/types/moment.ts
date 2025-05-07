import { MomentContent } from './common';

export interface EmojiParticle {
  id: string;
  emoji: string;
  x: number;
  y: number;
  vx: number;
  vy: number;
  rotation: number;
}

export interface DragState {
  emoji: EmojiParticle | null;
  offset: { x: number; y: number };
  velocity: { x: number; y: number };
}

export interface Dimensions {
  width: number;
  height: number;
}

export interface ParticleStyle {
  left: string;
  top: string;
  fontSize: string;
  transform: string;
  transition: string;
  zIndex: number;
}


// 순간일기 목록조회 파라미터
export interface GetMomentsParams {
  before?: string;
  limit?: number;
  startDate?: string;
  endDate?: string;
  userTags?: string;
}

// 순간일기 목록조회 응답
export interface MomentsResponse {
  data: MomentContent[];
  resInfo: {
    dataCount: number;
    nextCursor: string | null; // mock data 활용을 위한 타입 수정
    hasMore: boolean;
  };
}

// 순간일기 상세조회 응답
export interface MomentResponse {
  momentId: number;
  momentTime: string;
  images: string[];
  // content: string;
  tags: string[];
