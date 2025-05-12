export interface MomentContent {
  createdAt?: string;
  summaryId?: number;
  momentId?: number;
  momentTime: string;
  images: string[];
  content: string;
  tags: string[];
  imageCount?: number;
  tagCount?: number;
  orderInDay?: number;
}

export interface MomentCardProps {
  moment: MomentContent;
  isToday: boolean;
}

export interface ParsedTime {
  date: string;
  time: string;
}
