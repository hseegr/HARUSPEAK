import { useEffect, useState } from 'react';

import { MomentContent } from '@/types/common';

import { useMomentTagRecommend as useTagRecommendMutation } from './useTodayQuery';

interface UseMomentTagRecommendProps {
  moment: MomentContent;
  initialTags: string[];
}

export const useMomentTagRecommend = ({
  moment,
  initialTags,
}: UseMomentTagRecommendProps) => {
  const [tags, setTags] = useState<string[]>(initialTags);
  const [isLoading, setIsLoading] = useState(false);
  const { mutate: recommendTagMutation } = useTagRecommendMutation();

  // moment.tags가 변경될 때마다 로컬 상태 업데이트
  useEffect(() => {
    setTags(moment.tags);
  }, [moment.tags]);

  const handleGenerateTags = async () => {
    setIsLoading(true);
    recommendTagMutation(
      {
        tags,
        createdAt: moment.momentTime,
        content: moment.content,
      },
      {
        onSuccess: response => {
          setTags(prevTags => {
            const spaceLeft = 3 - prevTags.length;
            const tagsToAdd = response.recommendTags.slice(0, spaceLeft);
            return [...prevTags, ...tagsToAdd];
          });
        },
        onError: error => {
          console.error('태그 생성 실패:', error);
        },
        onSettled: () => {
          setIsLoading(false);
        },
      },
    );
  };

  return {
    tags,
    isLoading,
    handleGenerateTags,
  };
};
