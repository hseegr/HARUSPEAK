import { useQuery } from '@tanstack/react-query';

import { getMoment, getMoments } from '@/apis/momentApi';

interface GetMomentsParams {
  before?: string;
  limit?: number;
  startDate?: string;
  endDate?: string;
  userTags?: string;
}

interface MomentResponse {
  momentId: number;
  momentTime: string;
  images: string[];
  content: string;
  tags: string[];
}

export const useGetMoment = (momentId: string) =>
  useQuery<MomentResponse>({
    queryKey: ['moment', momentId],
    queryFn: () => getMoment(momentId),
  });

export const useGetMoments = (params?: GetMomentsParams) =>
  useQuery({
    queryKey: ['moments', params],
    queryFn: () => getMoments(params),
  });
