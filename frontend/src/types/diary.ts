import { MomentContent } from './common';
import { BaseDiary } from './library';

export interface DiaryResponse {
  summary: BaseDiary;
  moments: MomentContent[];
}
