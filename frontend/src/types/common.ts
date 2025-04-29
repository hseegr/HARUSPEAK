export interface MomentContent {
  momentTime: string;
  images: string[];
  content: string;
  tags: string[];
}

export interface MomentCardProps {
  moment: MomentContent;
  isToday: boolean;
}
